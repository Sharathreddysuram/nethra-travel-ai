import { createTheme } from '@mui/material';
const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#0b5563', dark: '#073c46', light: '#3d7f8c' },
    secondary: { main: '#e8871e', dark: '#c56f10' },
    background: { default: '#f6f3ee', paper: '#ffffff' },
    success: { main: '#1e7d4f' },
    error: { main: '#b3372f' },
  },
  typography: { fontFamily: "'Inter', system-ui, sans-serif", h3: { fontWeight: 800 }, h4: { fontWeight: 800 }, h5: { fontWeight: 700 }, h6: { fontWeight: 700 } },
  shape: { borderRadius: 14 },
  components: { MuiCard: { styleOverrides: { root: { boxShadow: '0 10px 30px rgba(11, 60, 70, 0.08)', border: '1px solid rgba(11, 60, 70, 0.06)' } } }, MuiButton: { styleOverrides: { root: { textTransform: 'none', fontWeight: 700 } } } },
});
export default theme;
