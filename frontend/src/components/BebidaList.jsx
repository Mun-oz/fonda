import { useState, useEffect } from 'react';
import { listarBebidas } from '../services/api';

export default function BebidaList() {
  const [bebidas, setBebidas] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState(null);

  const cargarBebidas = (nombreFiltro = '') => {
    setCargando(true);
    setError(null);
    listarBebidas(nombreFiltro)
      .then((data) => {
        setBebidas(data);
        setCargando(false);
      })
      .catch((err) => {
        setError(err.mensaje || 'Error al cargar el catálogo');
        setCargando(false);
      });
  };

  // Carga inicial al abrir la página
  useEffect(() => {
    cargarBebidas();
  }, []);

  const handleBuscar = (e) => {
    e.preventDefault();
    cargarBebidas(busqueda);
  };

  return (
    <div className="container mt-4">
      <h2>Catálogo de Bebidas</h2>
      
      {/* Barra de búsqueda */}
      <form onSubmit={handleBuscar} className="d-flex mb-3">
        <input
          type="text"
          className="form-control me-2"
          placeholder="Buscar por nombre..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn btn-outline-primary" type="submit">Buscar</button>
        <button 
          className="btn btn-outline-secondary ms-2" 
          type="button" 
          onClick={() => { setBusqueda(''); cargarBebidas(''); }}
        >
          Limpiar
        </button>
      </form>

      {/* Estados de carga y error */}
      {cargando && <div className="alert alert-info">Cargando bebidas...</div>}
      {error && <div className="alert alert-danger">{error}</div>}

      {/* Tabla del catálogo */}
      {!cargando && !error && (
        <div className="table-responsive">
          <table className="table table-striped table-hover align-middle">
            <thead className="table-dark">
              <tr>
                <th>Nombre</th>
                <th>Tipo</th>
                <th>Volumen</th>
                <th>Stock</th>
                <th>Precio</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {bebidas.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center">No se encontraron bebidas</td>
                </tr>
              ) : (
                bebidas.map((b) => (
                  <tr key={b.id}>
                    <td>{b.nombre}</td>
                    <td>
                      <span className={`badge ${b.tipo === 'ALCOHOLICA' ? 'bg-danger' : 'bg-success'}`}>
                        {b.tipo.replace('_', ' ')}
                      </span>
                    </td>
                    <td>{b.volumenML} ml</td>
                    <td>{b.stock}</td>
                    <td>${b.precio}</td>
                    <td>
                      {b.ventaRestringida ? (
                        <span className="badge bg-warning text-dark">Restringida</span>
                      ) : (
                        <span className="badge bg-primary">Libre</span>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}