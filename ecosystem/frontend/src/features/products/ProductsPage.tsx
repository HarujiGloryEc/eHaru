import { useEffect, useState } from 'react';
import {
  Box,
  Button,
  Chip,
  IconButton,
  MenuItem,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import AddIcon from '@mui/icons-material/Add';
import { useNavigate } from 'react-router-dom';
import { productApi } from './api/productApi';
import { storeApi } from '../stores/api/storeApi';
import type { ProductResponse } from './types';
import type { StoreResponse } from '../stores/types';

const STATUS_COLOR: Record<string, 'default' | 'success' | 'warning' | 'error'> = {
  DRAFT: 'default',
  ACTIVE: 'success',
  INACTIVE: 'warning',
  ARCHIVED: 'error',
};

function ProductsPage() {
  const navigate = useNavigate();
  const [stores, setStores] = useState<StoreResponse[]>([]);
  const [selectedStoreId, setSelectedStoreId] = useState('');
  const [products, setProducts] = useState<ProductResponse[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    storeApi.getAll().then((s) => {
      setStores(s);
      if (s.length > 0) setSelectedStoreId(s[0].id);
    });
  }, []);

  useEffect(() => {
    if (!selectedStoreId) return;
    setLoading(true);
    productApi
      .getByStore(selectedStoreId)
      .then(setProducts)
      .finally(() => setLoading(false));
  }, [selectedStoreId]);

  const handleDelete = async (id: string) => {
    if (window.confirm('Delete this product?')) {
      await productApi.remove(id);
      if (selectedStoreId) {
        productApi.getByStore(selectedStoreId).then(setProducts);
      }
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" component="h1">
          Products
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <TextField
            select
            label="Store"
            value={selectedStoreId}
            onChange={(e) => setSelectedStoreId(e.target.value)}
            size="small"
            sx={{ minWidth: 200 }}
          >
            {stores.map((s) => (
              <MenuItem key={s.id} value={s.id}>
                {s.name}
              </MenuItem>
            ))}
          </TextField>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => navigate(`/products/new?storeId=${selectedStoreId}`)}
            disabled={!selectedStoreId}
          >
            New Product
          </Button>
        </Box>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>SKU</TableCell>
              <TableCell>Price</TableCell>
              <TableCell>Stock</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={6}>Loading…</TableCell>
              </TableRow>
            ) : products.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6}>No products yet.</TableCell>
              </TableRow>
            ) : (
              products.map((p) => (
                <TableRow key={p.id}>
                  <TableCell>{p.name}</TableCell>
                  <TableCell>{p.sku ?? '—'}</TableCell>
                  <TableCell>${p.price.toFixed(2)}</TableCell>
                  <TableCell>{p.stockQuantity}</TableCell>
                  <TableCell>
                    <Chip label={p.status} color={STATUS_COLOR[p.status]} size="small" />
                  </TableCell>
                  <TableCell align="right">
                    <IconButton
                      size="small"
                      onClick={() => navigate(`/products/${p.id}/edit`)}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton size="small" color="error" onClick={() => handleDelete(p.id)}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}

export default ProductsPage;
