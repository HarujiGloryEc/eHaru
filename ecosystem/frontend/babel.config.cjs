module.exports = {
  presets: [
    ['@babel/preset-env', { targets: { node: 'current' } }],
    ['@babel/preset-react', { runtime: 'automatic' }],
    '@babel/preset-typescript',
  ],
  plugins: [
    // Vite's import.meta.env is not supported by Jest/Babel — replace with process.env stub.
    function transformImportMetaEnv({ types: t }) {
      return {
        visitor: {
          MetaProperty(path) {
            if (
              path.node.meta.name === 'import' &&
              path.node.property.name === 'meta'
            ) {
              path.replaceWith(
                t.objectExpression([
                  t.objectProperty(
                    t.identifier('env'),
                    t.objectExpression([
                      t.objectProperty(
                        t.identifier('VITE_API_BASE_URL'),
                        t.stringLiteral(process.env.VITE_API_BASE_URL || '/api')
                      ),
                    ])
                  ),
                ])
              );
            }
          },
        },
      };
    },
  ],
};
