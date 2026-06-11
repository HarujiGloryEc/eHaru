import { Routes, Route } from 'react-router-dom';
import AppLayout from '../shared/components/layout/AppLayout';
import DashboardPage from '../features/dashboard/DashboardPage';
import StoresPage from '../features/stores/StoresPage';
import CategoriesPage from '../features/categories/CategoriesPage';
import ProductsPage from '../features/products/ProductsPage';
import ProductFormPage from '../features/products/ProductFormPage';

function AppRouter() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route path="/" element={<DashboardPage />} />
        <Route path="/stores" element={<StoresPage />} />
        <Route path="/categories" element={<CategoriesPage />} />
        <Route path="/products" element={<ProductsPage />} />
        <Route path="/products/new" element={<ProductFormPage />} />
        <Route path="/products/:id/edit" element={<ProductFormPage />} />
      </Route>
      <Route path="*" element={<div>404 — Page not found</div>} />
    </Routes>
  );
}

export default AppRouter;
