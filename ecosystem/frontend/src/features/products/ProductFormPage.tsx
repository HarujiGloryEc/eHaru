import { useEffect, useState } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import {
  Box,
  Button,
  Divider,
  FormControl,
  FormControlLabel,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Switch,
  TextField,
  Typography,
} from '@mui/material';
import { productApi } from './api/productApi';
import { categoryApi } from '../categories/api/categoryApi';
import type { CategoryResponse } from '../categories/types';
import type { CreateProductRequest, ProductStatus, UpdateProductRequest } from './types';

const STATUSES: ProductStatus[] = ['DRAFT', 'ACTIVE', 'INACTIVE', 'ARCHIVED'];

function ProductFormPage() {
  const { id } = useParams<{ id?: string }>();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const storeId = searchParams.get('storeId') ?? '';

  const [categories, setCategories] = useState<CategoryResponse[]>([]);
  const [form, setForm] = useState({
    name: '',
    slug: '',
    description: '',
    sku: '',
    price: '',
    compareAtPrice: '',
    costPrice: '',
    stockQuantity: '0',
    lowStockThreshold: '5',
    trackInventory: true,
    status: 'DRAFT' as ProductStatus,
    primaryImageUrl: '',
    categoryId: '',
  });

  useEffect(() => {
    const loadStoreId = isEdit ? '' : storeId;
    if (loadStoreId) {
      categoryApi.getByStore(loadStoreId).then(setCategories);
    }
    if (isEdit && id) {
      productApi.getById(id).then((p) => {
        if (p.storeId) {
          categoryApi.getByStore(p.storeId).then(setCategories);
        }
        setForm({
          name: p.name,
          slug: p.slug,
          description: p.description ?? '',
          sku: p.sku ?? '',
          price: String(p.price),
          compareAtPrice: p.compareAtPrice ? String(p.compareAtPrice) : '',
          costPrice: p.costPrice ? String(p.costPrice) : '',
          stockQuantity: String(p.stockQuantity),
          lowStockThreshold: String(p.lowStockThreshold),
          trackInventory: p.trackInventory,
          status: p.status,
          primaryImageUrl: p.primaryImageUrl ?? '',
          categoryId: p.categoryId ?? '',
        });
      });
    }
  }, [id, isEdit, storeId]);

  const handleSubmit = async () => {
    if (isEdit && id) {
      const body: UpdateProductRequest = {
        name: form.name,
        slug: form.slug,
        description: form.description || undefined,
        sku: form.sku || undefined,
        price: parseFloat(form.price),
        compareAtPrice: form.compareAtPrice ? parseFloat(form.compareAtPrice) : undefined,
        costPrice: form.costPrice ? parseFloat(form.costPrice) : undefined,
        stockQuantity: parseInt(form.stockQuantity, 10),
        lowStockThreshold: parseInt(form.lowStockThreshold, 10),
        trackInventory: form.trackInventory,
        status: form.status,
        primaryImageUrl: form.primaryImageUrl || undefined,
        categoryId: form.categoryId || undefined,
      };
      await productApi.update(id, body);
    } else {
      const body: CreateProductRequest = {
        storeId,
        name: form.name,
        slug: form.slug,
        description: form.description || undefined,
        sku: form.sku || undefined,
        price: parseFloat(form.price),
        compareAtPrice: form.compareAtPrice ? parseFloat(form.compareAtPrice) : undefined,
        costPrice: form.costPrice ? parseFloat(form.costPrice) : undefined,
        stockQuantity: parseInt(form.stockQuantity, 10),
        lowStockThreshold: parseInt(form.lowStockThreshold, 10),
        trackInventory: form.trackInventory,
        status: form.status,
        primaryImageUrl: form.primaryImageUrl || undefined,
        categoryId: form.categoryId || undefined,
      };
      await productApi.create(body);
    }
    navigate('/products');
  };

  const set = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) =>
    setForm((f) => ({ ...f, [field]: e.target.value }));

  return (
    <Box>
      <Typography variant="h4" component="h1" sx={{ mb: 3 }}>
        {isEdit ? 'Edit Product' : 'New Product'}
      </Typography>

      <Paper sx={{ p: 3, display: 'flex', flexDirection: 'column', gap: 2, maxWidth: 720 }}>
        <Typography variant="h6">Basic Info</Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <TextField label="Name" value={form.name} onChange={set('name')} fullWidth required />
          <TextField label="Slug" value={form.slug} onChange={set('slug')} fullWidth required />
        </Box>
        <TextField
          label="Description"
          value={form.description}
          onChange={set('description')}
          fullWidth
          multiline
          rows={3}
        />
        <TextField label="SKU" value={form.sku} onChange={set('sku')} sx={{ maxWidth: 240 }} />

        <FormControl fullWidth>
          <InputLabel>Category</InputLabel>
          <Select
            value={form.categoryId}
            label="Category"
            onChange={(e) => setForm((f) => ({ ...f, categoryId: e.target.value }))}
          >
            <MenuItem value="">— None —</MenuItem>
            {categories.map((c) => (
              <MenuItem key={c.id} value={c.id}>
                {c.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <Divider />
        <Typography variant="h6">Pricing</Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <TextField
            label="Price"
            type="number"
            value={form.price}
            onChange={set('price')}
            required
            inputProps={{ min: 0, step: 0.01 }}
          />
          <TextField
            label="Compare-at Price"
            type="number"
            value={form.compareAtPrice}
            onChange={set('compareAtPrice')}
            inputProps={{ min: 0, step: 0.01 }}
          />
          <TextField
            label="Cost Price"
            type="number"
            value={form.costPrice}
            onChange={set('costPrice')}
            inputProps={{ min: 0, step: 0.01 }}
          />
        </Box>

        <Divider />
        <Typography variant="h6">Inventory</Typography>
        <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
          <TextField
            label="Stock Quantity"
            type="number"
            value={form.stockQuantity}
            onChange={set('stockQuantity')}
            inputProps={{ min: 0 }}
          />
          <TextField
            label="Low Stock Threshold"
            type="number"
            value={form.lowStockThreshold}
            onChange={set('lowStockThreshold')}
            inputProps={{ min: 0 }}
          />
          <FormControlLabel
            control={
              <Switch
                checked={form.trackInventory}
                onChange={(e) => setForm((f) => ({ ...f, trackInventory: e.target.checked }))}
              />
            }
            label="Track Inventory"
          />
        </Box>

        <Divider />
        <Typography variant="h6">Status & Media</Typography>
        <FormControl sx={{ maxWidth: 200 }}>
          <InputLabel>Status</InputLabel>
          <Select
            value={form.status}
            label="Status"
            onChange={(e) => setForm((f) => ({ ...f, status: e.target.value as ProductStatus }))}
          >
            {STATUSES.map((s) => (
              <MenuItem key={s} value={s}>
                {s}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          label="Primary Image URL"
          value={form.primaryImageUrl}
          onChange={set('primaryImageUrl')}
          fullWidth
        />

        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end', mt: 1 }}>
          <Button onClick={() => navigate('/products')}>Cancel</Button>
          <Button variant="contained" onClick={handleSubmit}>
            {isEdit ? 'Save Changes' : 'Create Product'}
          </Button>
        </Box>
      </Paper>
    </Box>
  );
}

export default ProductFormPage;
