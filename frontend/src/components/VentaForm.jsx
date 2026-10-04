import { useState, useEffect } from 'react';
import { listarBebidas, registrarVenta } from '../services/api';

export default function VentaForm({ onVentaRegistrada }) {
  const [bebidas, setBebidas] = useState([]);
  const [bebidaId, setBebidaId] = useState('');
  const [unidades, setUnidades] = useState(1);
  const [resultado, setResultado] = useState(null);

  // Cargar las bebidas al iniciar para llenar el <select>
  useEffect(() => {
    listarBebidas()
      .then((data) => {
        setBebidas(data);
        if (data.length > 0) {
          setBebidaId(data[0].id); // Selecciona la primera por defecto
        }
      })
      .catch(console.error);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setResultado(null); // Limpiamos mensajes anteriores
    
    try {
      const res = await registrarVenta(bebidaId, unidades);
      // Si pasa directo, es un 201 Created (Éxito)
      setResultado({ 
        estado: 'AUTORIZADA', 
        mensaje: `Venta exitosa. Total a pagar: $${res.total}` 
      });
      
      // Avisamos a la app que hay una nueva venta para actualizar historiales/stock
      if (onVentaRegistrada) onVentaRegistrada();
      setUnidades(1); // Reiniciamos el contador

    } catch (err) {
      // Si el backend lanza el 409 Conflict (Reglas de negocio)
      if (err.status === 409) {
        setResultado({ 
          estado: 'RECHAZADA', 
          mensaje: `Venta rechazada: ${err.mensaje}` 
        });
      } else {
        setResultado({ 
          estado: 'ERROR', 
          mensaje: 'Error de validación o conexión con el servidor.' 
        });
      }
    }
  };

  return (
    <div className="card shadow-sm border-0 mb-4">
      <div className="card-header bg-success text-white fw-bold">
        Registrar Venta
      </div>
      <div className="card-body bg-light">
        
        {/* Alertas dinámicas según el resultado de la venta */}
        {resultado && resultado.estado === 'AUTORIZADA' && (
          <div className="alert alert-success">{resultado.mensaje}</div>
        )}
        {resultado && resultado.estado === 'RECHAZADA' && (
          <div className="alert alert-warning text-dark fw-bold">{resultado.mensaje}</div>
        )}
        {resultado && resultado.estado === 'ERROR' && (
          <div className="alert alert-danger">{resultado.mensaje}</div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Seleccionar Bebida</label>
            <select 
              className="form-select" 
              value={bebidaId} 
              onChange={(e) => setBebidaId(e.target.value)}
              required
            >
              {bebidas.map(b => (
                <option key={b.id} value={b.id}>
                  {b.nombre} ({b.volumenML}ml) - Stock: {b.stock}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Unidades a comprar</label>
            <input 
              type="number" 
              className="form-control" 
              min="1"
              value={unidades} 
              onChange={(e) => setUnidades(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-success w-100">
            Procesar Venta
          </button>
        </form>
      </div>
    </div>
  );
}