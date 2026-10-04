import { useState } from 'react';
import { crearBebida } from '../services/api';

export default function BebidaForm({ onBebidaCreada }) {
  // Estado inicial del formulario
  const [formData, setFormData] = useState({
    nombre: '',
    tipo: 'ALCOHOLICA',
    volumenML: '',
    stock: '',
    gradosAlcohol: '',
    certificada: false,
    azucarPorLitro: '',
    ventaRestringida: false
  });
  
  const [errores, setErrores] = useState({});
  const [exito, setExito] = useState(false);

  // Manejador de cambios en los inputs
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value
    });
  };

  // Enviar el formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrores({});
    setExito(false);
    
    try {
      // Limpiamos la carga dependiendo del tipo de bebida para que coincida con el modelo
      const payload = { ...formData };
      if (payload.tipo === 'ALCOHOLICA') {
        payload.azucarPorLitro = null;
      } else {
        payload.gradosAlcohol = null;
        payload.certificada = false;
      }

      await crearBebida(payload);
      setExito(true);
      
      // Reiniciamos el formulario a su estado original
      setFormData({
        nombre: '', tipo: 'ALCOHOLICA', volumenML: '', stock: '',
        gradosAlcohol: '', certificada: false, azucarPorLitro: '', ventaRestringida: false
      });
      
      // Avisamos al componente padre (App) que recargue la lista
      if (onBebidaCreada) onBebidaCreada();
      
    } catch (err) {
      // Si el backend responde 400, guardamos los errores campo por campo
      if (err.campos) {
        setErrores(err.campos);
      } else {
        setErrores({ general: err.mensaje || 'Error de conexión con el servidor' });
      }
    }
  };

  return (
    <div className="card mb-4 shadow-sm border-0">
      <div className="card-header bg-primary text-white fw-bold">
        Registrar Nueva Bebida
      </div>
      <div className="card-body bg-light">
        {exito && <div className="alert alert-success">Bebida registrada con éxito.</div>}
        {errores.general && <div className="alert alert-danger">{errores.general}</div>}
        
        <form onSubmit={handleSubmit}>
          {/* Fila 1: Nombre y Tipo */}
          <div className="row mb-3">
            <div className="col-md-6">
              <label className="form-label">Nombre</label>
              <input type="text" className={`form-control ${errores.nombre ? 'is-invalid' : ''}`} name="nombre" value={formData.nombre} onChange={handleChange} />
              <div className="invalid-feedback">{errores.nombre}</div>
            </div>
            <div className="col-md-6">
              <label className="form-label">Tipo de Bebida</label>
              <select className="form-select" name="tipo" value={formData.tipo} onChange={handleChange}>
                <option value="ALCOHOLICA">Alcohólica</option>
                <option value="SIN_ALCOHOL">Sin Alcohol</option>
              </select>
            </div>
          </div>

          {/* Fila 2: Volumen y Stock */}
          <div className="row mb-3">
            <div className="col-md-6">
              <label className="form-label">Volumen (ml)</label>
              <input type="number" className={`form-control ${errores.volumenML ? 'is-invalid' : ''}`} name="volumenML" value={formData.volumenML} onChange={handleChange} />
              <div className="invalid-feedback">{errores.volumenML}</div>
            </div>
            <div className="col-md-6">
              <label className="form-label">Stock inicial</label>
              <input type="number" className={`form-control ${errores.stock ? 'is-invalid' : ''}`} name="stock" value={formData.stock} onChange={handleChange} />
              <div className="invalid-feedback">{errores.stock}</div>
            </div>
          </div>

          {/* Fila 3: Campos Dinámicos según el Tipo */}
          {formData.tipo === 'ALCOHOLICA' ? (
            <div className="row mb-3">
              <div className="col-md-6">
                <label className="form-label">Grados de Alcohol</label>
                <input type="number" step="0.1" className={`form-control ${errores.gradosAlcohol ? 'is-invalid' : ''}`} name="gradosAlcohol" value={formData.gradosAlcohol} onChange={handleChange} />
                <div className="invalid-feedback">{errores.gradosAlcohol}</div>
              </div>
              <div className="col-md-6 d-flex align-items-center mt-4">
                <div className="form-check">
                  <input type="checkbox" className="form-check-input" name="certificada" id="certificada" checked={formData.certificada} onChange={handleChange} />
                  <label className="form-check-label" htmlFor="certificada">Certificación de Proveedor</label>
                </div>
              </div>
            </div>
          ) : (
            <div className="row mb-3">
              <div className="col-md-6">
                <label className="form-label">Azúcar por Litro (g)</label>
                <input type="number" className={`form-control ${errores.azucarPorLitro ? 'is-invalid' : ''}`} name="azucarPorLitro" value={formData.azucarPorLitro} onChange={handleChange} />
                <div className="invalid-feedback">{errores.azucarPorLitro}</div>
              </div>
            </div>
          )}

          {/* Fila 4: Restricción y Botón */}
          <div className="d-flex justify-content-between align-items-center mt-4">
            <div className="form-check">
              <input type="checkbox" className="form-check-input" name="ventaRestringida" id="ventaRestringida" checked={formData.ventaRestringida} onChange={handleChange} />
              <label className="form-check-label text-danger" htmlFor="ventaRestringida">Marcar como Venta Restringida</label>
            </div>
            <button type="submit" className="btn btn-primary px-4">Guardar Bebida</button>
          </div>
        </form>
      </div>
    </div>
  );
}