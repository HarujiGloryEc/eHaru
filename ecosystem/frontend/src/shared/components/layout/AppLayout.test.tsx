import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { ThemeProvider } from '@mui/material/styles';
import { createTheme } from '@mui/material/styles';
import { store } from '../../store/store';
import AppLayout from './AppLayout';

const theme = createTheme();

function renderWithProviders(ui: React.ReactElement) {
  return render(
    <Provider store={store}>
      <MemoryRouter>
        <ThemeProvider theme={theme}>
          {ui}
        </ThemeProvider>
      </MemoryRouter>
    </Provider>
  );
}

describe('AppLayout', () => {
  it('renders the app bar with Haru Shop title', () => {
    renderWithProviders(<AppLayout />);
    expect(screen.getByText('Haru Shop')).toBeInTheDocument();
  });

  it('renders all nav items', () => {
    renderWithProviders(<AppLayout />);
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Stores')).toBeInTheDocument();
    expect(screen.getByText('Categories')).toBeInTheDocument();
    expect(screen.getByText('Products')).toBeInTheDocument();
  });
});
