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
import { storeApi } from './api/storeApi';
import type { CreateStoreRequest, StoreResponse, UpdateStoreRequest } from './types';

const STATUS_COLOR: Record<string, 'success' | 'warning' | 'error'> = {
  ACTIVE: 'success',
  INACTIVE: 'warning',
  SUSPENDED: 'error',
};

const PLACEHOLDER_MERCHANT_ID = '00000000-0000-0000-0000-000000000001';

function StoresPage() {
  const [stores, setStores] = useState<StoreResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editing, setEditing] = useState<StoreResponse | null>(null);
  const [form, setForm] = useState({ name: '', slug: '', description: '' });

  const load = () => {
    setLoading(true);
    storeApi
      .getAll()
      .then(setStores)
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', slug: '', description: '' });
    setDialogOpen(true);
  };

  const openEdit = (store: StoreResponse) => {
    setEditing(store);
    setForm({ name: store.name, slug: store.slug, description: store.description ?? '' });
    setDialogOpen(true);
  };

  const handleSave = async () => {
    if (editing) {
      const body: UpdateStoreRequest = {
        name: form.name || undefined,
        description: form.description || undefined,
      };
      await storeApi.update(editing.id, body);
    } else {
      const body: CreateStoreRequest = {
        name: form.name,
        slug: form.slug,
        description: form.description || undefined,
        merchantId: PLACEHOLDER_MERCHANT_ID,
      };
      await storeApi.create(body);
    }
    setDialogOpen(false);
    load();
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Delete this store?')) {
      await storeApi.remove(id);
      load();
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h4" component="h1">
          Stores
        </Typography>
        <Button variant="contained" onClick={openCreate}>
          New Store
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Slug</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Description</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={5}>Loading…</TableCell>
              </TableRow>
            ) : stores.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5}>No stores yet.</TableCell>
              </TableRow>
            ) : (
              stores.map((s) => (
                <TableRow key={s.id}>
                  <TableCell>{s.name}</TableCell>
                  <TableCell>{s.slug}</TableCell>
                  <TableCell>
                    <Chip label={s.status} color={STATUS_COLOR[s.status]} size="small" />
                  </TableCell>
                  <TableCell>{s.description ?? '—'}</TableCell>
                  <TableCell align="right">
                    <IconButton size="small" onClick={() => openEdit(s)}>
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton size="small" color="error" onClick={() => handleDelete(s.id)}>
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
        <DialogTitle>{editing ? 'Edit Store' : 'New Store'}</DialogTitle>
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
              helperText="lowercase, hyphens only"
            />
          )}
          <TextField
            label="Description"
            value={form.description}
            onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
            fullWidth
            multiline
            rows={3}
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

export default StoresPage;
