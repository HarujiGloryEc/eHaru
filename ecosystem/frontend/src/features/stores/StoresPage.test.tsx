import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import StoresPage from './StoresPage';
import { storeApi } from './api/storeApi';

jest.mock('./api/storeApi');

const mockedStoreApi = storeApi as jest.Mocked<typeof storeApi>;

describe('StoresPage', () => {
  beforeEach(() => {
    mockedStoreApi.getAll.mockResolvedValue([]);
  });

  it('renders heading', async () => {
    render(
      <MemoryRouter>
        <StoresPage />
      </MemoryRouter>,
    );
    expect(screen.getByText('Stores')).toBeInTheDocument();
  });

  it('shows empty message when no stores', async () => {
    mockedStoreApi.getAll.mockResolvedValue([]);
    render(
      <MemoryRouter>
        <StoresPage />
      </MemoryRouter>,
    );
    expect(await screen.findByText('No stores yet.')).toBeInTheDocument();
  });

  it('renders store rows', async () => {
    mockedStoreApi.getAll.mockResolvedValue([
      {
        id: '1',
        name: 'My Store',
        slug: 'my-store',
        status: 'ACTIVE',
        merchantId: 'mid',
        createdAt: '2026-01-01T00:00:00',
      },
    ]);
    render(
      <MemoryRouter>
        <StoresPage />
      </MemoryRouter>,
    );
    expect(await screen.findByText('My Store')).toBeInTheDocument();
    expect(screen.getByText('my-store')).toBeInTheDocument();
  });

  it('shows New Store button', () => {
    render(
      <MemoryRouter>
        <StoresPage />
      </MemoryRouter>,
    );
    expect(screen.getByText('New Store')).toBeInTheDocument();
  });
});
