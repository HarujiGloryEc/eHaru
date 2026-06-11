import { useEffect, useState } from 'react';
import {
  Box,
  Button,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
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
import { categoryApi } from './api/categoryApi';
import { storeApi } from '../stores/api/storeApi';
import type { CategoryResponse, CreateCategoryRequest, UpdateCategoryRequest } from './types';
import type { StoreResponse } from '../stores/types';

function CategoriesPage() {
  const [stores, setStores] = useState<StoreResponse[]>([]);
  const [selectedStoreId, setSelectedStoreId] = useState('');
  const [categories, setCategories] = useState<CategoryResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editing, setEditing] = useState<CategoryResponse | null>(null);
  const [form, setForm] = useState({ name: '', slug: '', description: '', displayOrder: '0' });

  useEffect(() => {
    storeApi.getAll().then((s) => {
      setStores(s);
      if (s.length > 0) setSelectedStoreId(s[0].id);
    });
  }, []);

  useEffect(() => {
    if (!selectedStoreId) return;
    setLoading(true);
    categoryApi
      .getByStore(selectedStoreId)
      .then(setCategories)
      .finally(() => setLoading(false));
  }, [selectedStoreId]);

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', slug: '', description: '', displayOrder: '0' });
    setDialogOpen(true);
  };

  const openEdit = (cat: CategoryResponse) => {
    setEditing(cat);
    setForm({
      name: cat.name,
      slug: cat.slug,
      description: cat.description ?? '',
      displayOrder: String(cat.displayOrder),
    });
    setDialogOpen(true);
  };

  const handleSave = async () => {
    if (editing) {
      const body: UpdateCategoryRequest = {
        name: form.name || undefined,
        description: form.description || undefined,
        displayOrder: parseInt(form.displayOrder, 10),
      };
      await categoryApi.update(editing.id, body);
    } else {
      const body: CreateCategoryRequest = {
        storeId: selectedStoreId,
        name: form.name,
        slug: form.slug,
        description: form.description || undefined,
        displayOrder: parseInt(form.displayOrder, 10),
      };
      await categoryApi.create(body);
    }
    setDialogOpen(false);
    if (selectedStoreId) {
      categoryApi.getByStore(selectedStoreId).then(setCategories);
    }
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Delete this category?')) {
      await categoryApi.remove(id);
      if (selectedStoreId) {
        categoryApi.getByStore(selectedStoreId).then(setCategories);
      }
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" component="h1">
          Categories
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
          <Button variant="contained" onClick={openCreate} disabled={!selectedStoreId}>
            New Category
          </Button>
        </Box>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Slug</TableCell>
              <TableCell>Order</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Parent</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={6}>Loading…</TableCell>
              </TableRow>
            ) : categories.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6}>No categories yet.</TableCell>
              </TableRow>
            ) : (
              categories.map((c) => (
                <TableRow key={c.id}>
                  <TableCell>{c.name}</TableCell>
                  <TableCell>{c.slug}</TableCell>
                  <TableCell>{c.displayOrder}</TableCell>
                  <TableCell>
                    <Chip
                      label={c.isActive ? 'Active' : 'Inactive'}
                      color={c.isActive ? 'success' : 'default'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>{c.parentId ? c.parentId.slice(0, 8) + '…' : '—'}</TableCell>
                  <TableCell align="right">
                    <IconButton size="small" onClick={() => openEdit(c)}>
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton size="small" color="error" onClick={() => handleDelete(c.id)}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editing ? 'Edit Category' : 'New Category'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
          <TextField
            label="Name"
            value={form.name}
            onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
            fullWidth
          />
          {!editing && (
            <TextField
              label="Slug"
              value={form.slug}
              onChange={(e) => setForm((f) => ({ ...f, slug: e.target.value }))}
              fullWidth
            />
          )}
          <TextField
            label="Description"
            value={form.description}
            onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
            fullWidth
            multiline
            rows={2}
          />
          <TextField
            label="Display Order"
            type="number"
            value={form.displayOrder}
            onChange={(e) => setForm((f) => ({ ...f, displayOrder: e.target.value }))}
            fullWidth
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleSave}>
            {editing ? 'Save' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default CategoriesPage;
