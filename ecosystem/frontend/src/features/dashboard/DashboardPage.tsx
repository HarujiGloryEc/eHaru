import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Card, CardActionArea, CardContent, Typography } from '@mui/material';
import StoreIcon from '@mui/icons-material/Store';
import CategoryIcon from '@mui/icons-material/Category';
import InventoryIcon from '@mui/icons-material/Inventory';
import { storeApi } from '../stores/api/storeApi';

function StatCard({
  label,
  count,
  icon,
  path,
}: {
  label: string;
  count: number;
  icon: React.ReactNode;
  path: string;
}) {
  const navigate = useNavigate();
  return (
    <Card sx={{ minWidth: 180 }}>
      <CardActionArea onClick={() => navigate(path)}>
        <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, color: 'text.secondary' }}>
            {icon}
            <Typography variant="body2">{label}</Typography>
          </Box>
          <Typography variant="h3" component="div">
            {count}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}

function DashboardPage() {
  const [storeCount, setStoreCount] = useState(0);

  useEffect(() => {
    storeApi.getAll().then((stores) => setStoreCount(stores.length));
  }, []);

  return (
    <Box>
      <Typography variant="h4" component="h1" sx={{ mb: 3 }}>
        Dashboard
      </Typography>
      <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap' }}>
        <StatCard label="Stores" count={storeCount} icon={<StoreIcon />} path="/stores" />
        <StatCard label="Categories" count={0} icon={<CategoryIcon />} path="/categories" />
        <StatCard label="Products" count={0} icon={<InventoryIcon />} path="/products" />
      </Box>
    </Box>
  );
}

export default DashboardPage;
