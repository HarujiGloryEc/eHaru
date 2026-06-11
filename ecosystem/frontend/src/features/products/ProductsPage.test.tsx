import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ProductsPage from './ProductsPage';
import { productApi } from './api/productApi';
import { storeApi } from '../stores/api/storeApi';

jest.mock('./api/productApi');
jest.mock('../stores/api/storeApi');

const mockedProductApi = productApi as jest.Mocked<typeof productApi>;
const mockedStoreApi = storeApi as jest.Mocked<typeof storeApi>;

describe('ProductsPage', () => {
  beforeEach(() => {
    mockedStoreApi.getAll.mockResolvedValue([]);
    mockedProductApi.getByStore.mockResolvedValue([]);
  });

  it('renders heading', () => {
    render(
      <MemoryRouter>
        <ProductsPage />
      </MemoryRouter>,
    );
    expect(screen.getByText('Products')).toBeInTheDocument();
  });

  it('shows empty message when no products', async () => {
    render(
      <MemoryRouter>
        <ProductsPage />
      </MemoryRouter>,
    );
    expect(await screen.findByText('No products yet.')).toBeInTheDocument();
  });

  it('renders product rows when data loaded', async () => {
    mockedStoreApi.getAll.mockResolvedValue([
      { id: 'sid', name: 'Store', slug: 'store', status: 'ACTIVE', merchantId: 'mid', createdAt: '2026-01-01T00:00:00' },
    ]);
    mockedProductApi.getByStore.mockResolvedValue([
      {
        id: 'p1',
        name: 'Widget',
        slug: 'widget',
        price: 9.99,
        stockQuantity: 100,
        lowStockThreshold: 5,
        trackInventory: true,
        status: 'ACTIVE',
        storeId: 'sid',
        createdAt: '2026-01-01T00:00:00',
      },
    ]);
    render(
      <MemoryRouter>
        <ProductsPage />
      </MemoryRouter>,
    );
    expect(await screen.findByText('Widget')).toBeInTheDocument();
    expect(screen.getByText('$9.99')).toBeInTheDocument();
  });
});
