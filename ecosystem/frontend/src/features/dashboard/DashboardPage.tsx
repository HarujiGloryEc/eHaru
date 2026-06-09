import { Typography, Box } from '@mui/material';

function DashboardPage() {
  return (
    <Box>
      <Typography variant="h4" component="h1">
        Dashboard
      </Typography>
      <Typography variant="body1" sx={{ mt: 2 }}>
        Welcome to Haru Shop.
      </Typography>
    </Box>
  );
}

export default DashboardPage;
