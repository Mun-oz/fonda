import { Container, Row, Col } from "react-bootstrap";
import BebidaList from "./components/BebidaList";
import BebidaForm from "./components/BebidaForm";
import VentaForm from "./components/VentaForm";

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
  return (
    <Container className="py-4">
      <h1 className="mb-1">Fonda San Belarmino</h1>
      <p className="text-muted">Control de bebidas y ventas</p>
      {/* TODO: montar aqui los componentes de la interfaz. */}


      <Row>
        {/* Columna izquierda: Formularios */}
        <Col lg={4} className="mb-4">
          <BebidaForm />
          <VentaForm />
        </Col>

      {/* Columna derecha: El catálogo */}
        <Col lg={8}>
          <BebidaList />
        </Col>
      </Row>
    </Container>
  );
}