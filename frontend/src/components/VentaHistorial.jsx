import { useState, useEffect } from 'react';
import { listarVentas } from '../services/api';

export default function VentaHistorial() {
  const [ventas, setVentas] = useState([]);

  useEffect(() => {
    listarVentas()
      .then(setVentas)
      .catch(console.error);
  }, []);

  return (
    <div className="card mt-4 shadow-sm border-0">
      <div className="card-header bg-dark text-white fw-bold">
        Historial de Transacciones
      </div>
      <div className="card-body bg-light p-0">
        <div className="table-responsive">
          <table className="table table-striped table-hover m-0 align-middle">
            <thead className="table-secondary">
              <tr>
                <th>ID</th>
                <th>Unidades</th>
                <th>Total</th>
                <th>Estado</th>
                <th>Motivo (si aplica)</th>
              </tr>
            </thead>
            <tbody>
              {ventas.length === 0 ? (
                <tr>
                  <td colSpan="5" className="text-center py-3">No hay ventas registradas aún</td>
                </tr>
              ) : (
                ventas.map((v) => (
                  <tr key={v.id}>
                    <td>#{v.id}</td>
                    <td>{v.unidades}</td>
                    <td>{v.total ? `$${v.total}` : '-'}</td>
                    <td>
                      <span className={`badge ${v.estado === 'AUTORIZADA' ? 'bg-success' : 'bg-danger'}`}>
                        {v.estado}
                      </span>
                    </td>
                    <td className="text-muted small">{v.motivoRechazo || 'Ninguno'}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}