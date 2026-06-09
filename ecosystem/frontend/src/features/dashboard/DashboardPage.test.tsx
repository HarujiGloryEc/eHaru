import { render, screen } from '@testing-library/react';
import DashboardPage from './DashboardPage';

describe('DashboardPage', () => {
  it('renders Dashboard heading', () => {
    render(<DashboardPage />);
    expect(screen.getByRole('heading', { name: /dashboard/i })).toBeInTheDocument();
  });

  it('renders welcome message', () => {
    render(<DashboardPage />);
    expect(screen.getByText(/welcome to haru shop/i)).toBeInTheDocument();
  });
});
