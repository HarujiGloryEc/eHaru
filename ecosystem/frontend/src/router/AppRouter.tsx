import { Routes, Route } from 'react-router-dom';
import AppLayout from '../shared/components/layout/AppLayout';
import DashboardPage from '../features/dashboard/DashboardPage';

function AppRouter() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route path="/" element={<DashboardPage />} />
      </Route>
      <Route path="*" element={<div>404 — Page not found</div>} />
    </Routes>
  );
}

export default AppRouter;
