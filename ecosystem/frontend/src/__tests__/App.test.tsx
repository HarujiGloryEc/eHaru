import { render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { MemoryRouter } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import { createTheme } from '@mui/material/styles';
import App from '../App';
import { store } from '../shared/store/store';

const theme = createTheme();

describe('App', () => {
  it('renders without crashing', () => {
    expect(() =>
      render(
        <Provider store={store}>
          <MemoryRouter>
            <ThemeProvider theme={theme}>
              <App />
            </ThemeProvider>
          </MemoryRouter>
        </Provider>
      )
    ).not.toThrow();
  });
});
