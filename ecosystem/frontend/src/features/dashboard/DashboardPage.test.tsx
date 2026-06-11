import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import DashboardPage from './DashboardPage';
import { storeApi } from '../stores/api/storeApi';

jest.mock('../stores/api/storeApi');

const mockedStoreApi = storeApi as jest.Mocked<typeof storeApi>;

describe('DashboardPage', () => {
  beforeEach(() => {
    mockedStoreApi.getAll.mockResolvedValue([]);
  });

  it('renders Dashboard heading', () => {
    render(
      <MemoryRouter>
        <DashboardPage />
      </MemoryRouter>,
    );
    expect(screen.getByRole('heading', { name: /dashboard/i })).toBeInTheDocument();
  });

  it('shows stat cards for Stores, Categories and Products', () => {
    render(
      <MemoryRouter>
        <DashboardPage />
      </MemoryRouter>,
    );
    expect(screen.getByText('Stores')).toBeInTheDocument();
    expect(screen.getByText('Categories')).toBeInTheDocument();
    expect(screen.getByText('Products')).toBeInTheDocument();
  });
});
