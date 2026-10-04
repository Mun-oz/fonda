import { useState } from "react";
import { Container, Row, Col } from "react-bootstrap";
import BebidaList from "./components/BebidaList";
import BebidaForm from "./components/BebidaForm";
import VentaForm from "./components/VentaForm";
import VentaHistorial from "./components/VentaHistorial";

/**
 * Estructura sugerida de la interfaz. Cada bloque es un componente propio
 * dentro de src/components/:
 *
 *   BebidaList      tabla del catalogo, con filtro por nombre
 *   BebidaForm      alta y edicion de una bebida
 *   VentaForm       registro de una venta
 *   VentaHistorial  listado de ventas con su estado y motivo
 *
 * Ningun componente calcula precios ni decide si una venta se autoriza:
 * esos datos vienen del backend.
 */
export default function App() {
  // Este estado nos servirá como gatillo para recargar todo
  const [actualizar, setActualizar] = useState(0);
  const recargarPantalla = () => setActualizar(actualizar + 1);

  return (
    <Container className="py-4">
      <h1 className="mb-1">Fonda San Belarmino</h1>
      <p className="text-muted">Control de bebidas y ventas</p>
      {/* TODO: montar aqui los componentes de la interfaz. */}


      <Row>
        {/* Columna izquierda: Formularios */}
        <Col lg={4} className="mb-4">
          <BebidaForm onBebidaCreada={recargarPantalla} />
        {/* Le pasamos el gatillo para que avise cuando se haga una venta */}
          <VentaForm onVentaRegistrada={recargarPantalla} key={`vf-${actualizar}`} />
        </Col>

       {/* Columna derecha: El catálogo */}
        <Col lg={8}>
          <BebidaList key={`bl-${actualizar}`} />
          <VentaHistorial key={`vh-${actualizar}`} />
        </Col>
      </Row>
    </Container>
  );
}