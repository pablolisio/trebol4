--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.2
-- Dumped by pg_dump version 9.4.2
-- Started on 2016-04-22 16:20:19

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE "FacturarOnLineDB";
--
-- TOC entry 2290 (class 1262 OID 16662)
-- Name: FacturarOnLineDB; Type: DATABASE; Schema: -; Owner: facturaronline
--

CREATE DATABASE "FacturarOnLineDB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Spanish_Argentina.1252' LC_CTYPE = 'Spanish_Argentina.1252';


ALTER DATABASE "FacturarOnLineDB" OWNER TO facturaronline;

\connect "FacturarOnLineDB"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 6 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2291 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 217 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2293 (class 0 OID 0)
-- Dependencies: 217
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 19671)
-- Name: banco; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE banco (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255) NOT NULL
);


ALTER TABLE banco OWNER TO facturaronline;

--
-- TOC entry 173 (class 1259 OID 19674)
-- Name: banco_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE banco_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE banco_id_seq OWNER TO facturaronline;

--
-- TOC entry 2294 (class 0 OID 0)
-- Dependencies: 173
-- Name: banco_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE banco_id_seq OWNED BY banco.id;


--
-- TOC entry 174 (class 1259 OID 19676)
-- Name: caja_chica; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE caja_chica (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    fecha timestamp without time zone NOT NULL,
    detalle character varying(255) NOT NULL,
    mes date NOT NULL,
    monto numeric NOT NULL,
    solicitado_por_otros boolean NOT NULL,
    solicitado_por_todos boolean NOT NULL,
    usuario_solicitante_id integer
);


ALTER TABLE caja_chica OWNER TO facturaronline;

--
-- TOC entry 175 (class 1259 OID 19682)
-- Name: caja_chica_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE caja_chica_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE caja_chica_id_seq OWNER TO facturaronline;

--
-- TOC entry 2295 (class 0 OID 0)
-- Dependencies: 175
-- Name: caja_chica_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE caja_chica_id_seq OWNED BY caja_chica.id;


--
-- TOC entry 176 (class 1259 OID 19684)
-- Name: carga_hs; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE carga_hs (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    detalle character varying(255) NOT NULL,
    fecha timestamp without time zone NOT NULL,
    monto numeric NOT NULL
);


ALTER TABLE carga_hs OWNER TO facturaronline;

--
-- TOC entry 177 (class 1259 OID 19690)
-- Name: carga_hs_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE carga_hs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE carga_hs_id_seq OWNER TO facturaronline;

--
-- TOC entry 2296 (class 0 OID 0)
-- Dependencies: 177
-- Name: carga_hs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE carga_hs_id_seq OWNED BY carga_hs.id;


--
-- TOC entry 206 (class 1259 OID 20002)
-- Name: cliente; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE cliente (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    razon_social character varying(255) NOT NULL,
    cuit_cuil character varying(13) NOT NULL,
    domicilio character varying(255),
    mascara_modo_cobros integer NOT NULL,
    telefono1 character varying,
    telefono2 character varying,
    contacto character varying,
    mails character varying,
    referencias character varying,
    observaciones character varying,
    porcentaje_directivo_bianca numeric NOT NULL,
    porcentaje_directivo_paul numeric NOT NULL,
    porcentaje_directivo_cristian numeric NOT NULL,
    porcentaje_directivo_juan numeric NOT NULL
);


ALTER TABLE cliente OWNER TO facturaronline;

--
-- TOC entry 207 (class 1259 OID 20008)
-- Name: cliente_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE cliente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cliente_id_seq OWNER TO facturaronline;

--
-- TOC entry 2297 (class 0 OID 0)
-- Dependencies: 207
-- Name: cliente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE cliente_id_seq OWNED BY cliente.id;


--
-- TOC entry 178 (class 1259 OID 19692)
-- Name: cobro_alternativo; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE cobro_alternativo (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    titular character varying(255) NOT NULL,
    cuit_cuil character varying(13) NOT NULL,
    cuenta_bancaria_id integer NOT NULL,
    proveedor_id integer NOT NULL
);


ALTER TABLE cobro_alternativo OWNER TO facturaronline;

--
-- TOC entry 179 (class 1259 OID 19695)
-- Name: cobro_alternativo_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE cobro_alternativo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cobro_alternativo_id_seq OWNER TO facturaronline;

--
-- TOC entry 2298 (class 0 OID 0)
-- Dependencies: 179
-- Name: cobro_alternativo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE cobro_alternativo_id_seq OWNED BY cobro_alternativo.id;


--
-- TOC entry 180 (class 1259 OID 19697)
-- Name: cuenta_bancaria; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE cuenta_bancaria (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    tipo_cuenta_id integer,
    nro_cuenta character varying(255),
    banco_id integer,
    cbu character varying(22) NOT NULL
);


ALTER TABLE cuenta_bancaria OWNER TO facturaronline;

--
-- TOC entry 181 (class 1259 OID 19700)
-- Name: cuenta_bancaria_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE cuenta_bancaria_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cuenta_bancaria_id_seq OWNER TO facturaronline;

--
-- TOC entry 2299 (class 0 OID 0)
-- Dependencies: 181
-- Name: cuenta_bancaria_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE cuenta_bancaria_id_seq OWNED BY cuenta_bancaria.id;


--
-- TOC entry 182 (class 1259 OID 19702)
-- Name: estado_factura_compra; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE estado_factura_compra (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255),
    descripcion character varying(255)
);


ALTER TABLE estado_factura_compra OWNER TO facturaronline;

--
-- TOC entry 183 (class 1259 OID 19708)
-- Name: estado_factura_compra_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE estado_factura_compra_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE estado_factura_compra_id_seq OWNER TO facturaronline;

--
-- TOC entry 2300 (class 0 OID 0)
-- Dependencies: 183
-- Name: estado_factura_compra_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE estado_factura_compra_id_seq OWNED BY estado_factura_compra.id;


--
-- TOC entry 208 (class 1259 OID 20013)
-- Name: estado_solicitud_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE estado_solicitud_pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255),
    descripcion character varying(255)
);


ALTER TABLE estado_solicitud_pago OWNER TO facturaronline;

--
-- TOC entry 209 (class 1259 OID 20019)
-- Name: estado_solicitud_pago_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE estado_solicitud_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE estado_solicitud_pago_id_seq OWNER TO facturaronline;

--
-- TOC entry 2301 (class 0 OID 0)
-- Dependencies: 209
-- Name: estado_solicitud_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE estado_solicitud_pago_id_seq OWNED BY estado_solicitud_pago.id;


--
-- TOC entry 184 (class 1259 OID 19710)
-- Name: evento; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE evento (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255) NOT NULL,
    mostrar_a_solicitantes boolean
);


ALTER TABLE evento OWNER TO facturaronline;

--
-- TOC entry 185 (class 1259 OID 19713)
-- Name: evento_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE evento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE evento_id_seq OWNER TO facturaronline;

--
-- TOC entry 2302 (class 0 OID 0)
-- Dependencies: 185
-- Name: evento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE evento_id_seq OWNED BY evento.id;


--
-- TOC entry 186 (class 1259 OID 19715)
-- Name: factura_compra; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE factura_compra (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    fecha timestamp without time zone NOT NULL,
    mes_impositivo timestamp without time zone NOT NULL,
    proveedor_id integer NOT NULL,
    nro character varying(255) NOT NULL,
    subtotal numeric NOT NULL,
    iva numeric NOT NULL,
    perc_iva numeric,
    perc_iibb numeric,
    estado_factura_compra_id integer NOT NULL,
    tipo_factura_id integer NOT NULL
);


ALTER TABLE factura_compra OWNER TO facturaronline;

--
-- TOC entry 187 (class 1259 OID 19721)
-- Name: factura_compra_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE factura_compra_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE factura_compra_id_seq OWNER TO facturaronline;

--
-- TOC entry 2303 (class 0 OID 0)
-- Dependencies: 187
-- Name: factura_compra_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE factura_compra_id_seq OWNED BY factura_compra.id;


--
-- TOC entry 188 (class 1259 OID 19723)
-- Name: factura_compra_orden_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE factura_compra_orden_pago (
    factura_compra_id integer NOT NULL,
    orden_pago_id integer NOT NULL,
    borrado boolean NOT NULL
);


ALTER TABLE factura_compra_orden_pago OWNER TO facturaronline;

--
-- TOC entry 210 (class 1259 OID 20024)
-- Name: factura_solicitud_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE factura_solicitud_pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    factura_compra_id integer,
    tipo_factura_compra_id integer,
    nro_factura_compra character varying(255),
    total numeric NOT NULL,
    solicitud_pago_id integer NOT NULL
);


ALTER TABLE factura_solicitud_pago OWNER TO facturaronline;

--
-- TOC entry 2304 (class 0 OID 0)
-- Dependencies: 210
-- Name: TABLE factura_solicitud_pago; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON TABLE factura_solicitud_pago IS 'Son las Facturas de Compra usadas en la SOLICITUD de Pago';


--
-- TOC entry 2305 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN factura_solicitud_pago.factura_compra_id; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON COLUMN factura_solicitud_pago.factura_compra_id IS 'si existe la factura compra, se llena este campo';


--
-- TOC entry 2306 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN factura_solicitud_pago.tipo_factura_compra_id; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON COLUMN factura_solicitud_pago.tipo_factura_compra_id IS 'si no existe la factura compra, se llena este campo';


--
-- TOC entry 2307 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN factura_solicitud_pago.nro_factura_compra; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON COLUMN factura_solicitud_pago.nro_factura_compra IS 'si no existe la factura compra, se llena este campo';


--
-- TOC entry 211 (class 1259 OID 20030)
-- Name: factura_solicitud_pago_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE factura_solicitud_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE factura_solicitud_pago_id_seq OWNER TO facturaronline;

--
-- TOC entry 2308 (class 0 OID 0)
-- Dependencies: 211
-- Name: factura_solicitud_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE factura_solicitud_pago_id_seq OWNED BY factura_solicitud_pago.id;


--
-- TOC entry 212 (class 1259 OID 20035)
-- Name: solicitud_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE solicitud_pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nro character varying(255) NOT NULL,
    fecha timestamp without time zone NOT NULL,
    proveedor_id integer,
    razon_social character varying(255),
    cuit_cuil character varying(13),
    cbu character varying(22),
    con_factura boolean NOT NULL,
    evento_id integer NOT NULL,
    concepto character varying(255),
    observaciones character varying,
    cliente_id integer,
    nro_factura_venta character varying(255),
    usuario_solicitante_id integer NOT NULL,
    estado_solicitud_pago_id integer NOT NULL,
    fecha_probable_pago timestamp without time zone NOT NULL
);


ALTER TABLE solicitud_pago OWNER TO facturaronline;

--
-- TOC entry 2309 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN solicitud_pago.proveedor_id; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON COLUMN solicitud_pago.proveedor_id IS 'si existe el proveedor, se llena este campo';


--
-- TOC entry 2310 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN solicitud_pago.razon_social; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON COLUMN solicitud_pago.razon_social IS 'si no existe el proveedor, se llena este campo';


--
-- TOC entry 2311 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN solicitud_pago.cuit_cuil; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON COLUMN solicitud_pago.cuit_cuil IS 'si no existe el proveedor, se llena este campo';


--
-- TOC entry 2312 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN solicitud_pago.cbu; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON COLUMN solicitud_pago.cbu IS 'si no existe el proveedor, y se necesita transferencia se llena este campo';


--
-- TOC entry 214 (class 1259 OID 20046)
-- Name: get_nros_factura_compra; Type: VIEW; Schema: public; Owner: facturaronline
--

CREATE VIEW get_nros_factura_compra AS
 SELECT nros_factura_compra.solicitud_pago_id,
    nros_factura_compra.nro_factura_compra
   FROM ( SELECT f.solicitud_pago_id,
            fc.nro AS nro_factura_compra
           FROM factura_solicitud_pago f,
            factura_compra fc
          WHERE ((((f.borrado = false) AND (fc.borrado = false)) AND (f.factura_compra_id = fc.id)) AND (f.nro_factura_compra IS NULL))
        UNION
         SELECT f.solicitud_pago_id,
            f.nro_factura_compra
           FROM factura_solicitud_pago f
          WHERE ((f.borrado = false) AND (f.nro_factura_compra IS NOT NULL))
        UNION
         SELECT solicitud_pago.id AS solicitud_pago_id,
            '<Sin Factura>'::character varying AS nro_factura_compra
           FROM solicitud_pago
          WHERE ((solicitud_pago.borrado = false) AND (solicitud_pago.con_factura = false))) nros_factura_compra
  ORDER BY nros_factura_compra.solicitud_pago_id;


ALTER TABLE get_nros_factura_compra OWNER TO facturaronline;

--
-- TOC entry 2313 (class 0 OID 0)
-- Dependencies: 214
-- Name: VIEW get_nros_factura_compra; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON VIEW get_nros_factura_compra IS 'Obtiene un listado de los id de solicitudes de pago, junto con los numero de facturas (si es que las solicitudes tienen facturas). Tambien muestro los SinFactura.';


--
-- TOC entry 189 (class 1259 OID 19726)
-- Name: modo_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE modo_pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255),
    descripcion character varying(255)
);


ALTER TABLE modo_pago OWNER TO facturaronline;

--
-- TOC entry 190 (class 1259 OID 19732)
-- Name: modo_pago_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE modo_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE modo_pago_id_seq OWNER TO facturaronline;

--
-- TOC entry 2314 (class 0 OID 0)
-- Dependencies: 190
-- Name: modo_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE modo_pago_id_seq OWNED BY modo_pago.id;


--
-- TOC entry 191 (class 1259 OID 19734)
-- Name: orden_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE orden_pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    fecha timestamp without time zone NOT NULL,
    nro character varying(255) NOT NULL,
    evento_id integer NOT NULL,
    concepto character varying(255),
    usuario_solicitante_id integer NOT NULL,
    solicitud_pago_id integer
);


ALTER TABLE orden_pago OWNER TO facturaronline;

--
-- TOC entry 192 (class 1259 OID 19740)
-- Name: orden_pago_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE orden_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE orden_pago_id_seq OWNER TO facturaronline;

--
-- TOC entry 2315 (class 0 OID 0)
-- Dependencies: 192
-- Name: orden_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE orden_pago_id_seq OWNED BY orden_pago.id;


--
-- TOC entry 193 (class 1259 OID 19742)
-- Name: pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    modo_pago_id integer NOT NULL,
    nro_cheque character varying(255),
    banco_id integer,
    fecha_cheque timestamp without time zone,
    cobro_alternativo_id integer,
    cuenta_bancaria_id integer,
    importe numeric NOT NULL,
    orden_pago_id integer NOT NULL,
    cuit_cuil character varying(13)
);


ALTER TABLE pago OWNER TO facturaronline;

--
-- TOC entry 194 (class 1259 OID 19748)
-- Name: pago_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pago_id_seq OWNER TO facturaronline;

--
-- TOC entry 2316 (class 0 OID 0)
-- Dependencies: 194
-- Name: pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE pago_id_seq OWNED BY pago.id;


--
-- TOC entry 215 (class 1259 OID 20056)
-- Name: pago_solicitud_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE pago_solicitud_pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    modo_pago_id integer NOT NULL,
    fecha_cheque timestamp without time zone,
    cobro_alternativo_id integer,
    cuenta_bancaria_id integer,
    cuit_cuil character varying(13),
    importe numeric NOT NULL,
    solicitud_pago_id integer NOT NULL
);


ALTER TABLE pago_solicitud_pago OWNER TO facturaronline;

--
-- TOC entry 2317 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE pago_solicitud_pago; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON TABLE pago_solicitud_pago IS 'Son los Pagos utilizados en la SOLICITUD de Pago';


--
-- TOC entry 216 (class 1259 OID 20062)
-- Name: pago_solicitud_pago_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE pago_solicitud_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pago_solicitud_pago_id_seq OWNER TO facturaronline;

--
-- TOC entry 2318 (class 0 OID 0)
-- Dependencies: 216
-- Name: pago_solicitud_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE pago_solicitud_pago_id_seq OWNED BY pago_solicitud_pago.id;


--
-- TOC entry 195 (class 1259 OID 19758)
-- Name: proveedor; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE proveedor (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    razon_social character varying(255) NOT NULL,
    cuit_cuil character varying(13),
    cuenta_bancaria_id integer,
    mascara_modo_pago integer NOT NULL
);


ALTER TABLE proveedor OWNER TO facturaronline;

--
-- TOC entry 196 (class 1259 OID 19761)
-- Name: proveedor_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE proveedor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE proveedor_id_seq OWNER TO facturaronline;

--
-- TOC entry 2319 (class 0 OID 0)
-- Dependencies: 196
-- Name: proveedor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE proveedor_id_seq OWNED BY proveedor.id;


--
-- TOC entry 197 (class 1259 OID 19763)
-- Name: rol; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE rol (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    descripcion character varying(255),
    nombre_rol character varying(255) NOT NULL
);


ALTER TABLE rol OWNER TO facturaronline;

--
-- TOC entry 198 (class 1259 OID 19769)
-- Name: rol_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE rol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rol_id_seq OWNER TO facturaronline;

--
-- TOC entry 2320 (class 0 OID 0)
-- Dependencies: 198
-- Name: rol_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE rol_id_seq OWNED BY rol.id;


--
-- TOC entry 213 (class 1259 OID 20041)
-- Name: solicitud_pago_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE solicitud_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE solicitud_pago_id_seq OWNER TO facturaronline;

--
-- TOC entry 2321 (class 0 OID 0)
-- Dependencies: 213
-- Name: solicitud_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE solicitud_pago_id_seq OWNED BY solicitud_pago.id;


--
-- TOC entry 199 (class 1259 OID 19784)
-- Name: tipo_cuenta; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE tipo_cuenta (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255),
    descripcion character varying(255)
);


ALTER TABLE tipo_cuenta OWNER TO facturaronline;

--
-- TOC entry 200 (class 1259 OID 19790)
-- Name: tipo_cuenta_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE tipo_cuenta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tipo_cuenta_id_seq OWNER TO facturaronline;

--
-- TOC entry 2322 (class 0 OID 0)
-- Dependencies: 200
-- Name: tipo_cuenta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE tipo_cuenta_id_seq OWNED BY tipo_cuenta.id;


--
-- TOC entry 201 (class 1259 OID 19792)
-- Name: tipo_factura; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE tipo_factura (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255),
    descripcion character varying(255)
);


ALTER TABLE tipo_factura OWNER TO facturaronline;

--
-- TOC entry 202 (class 1259 OID 19798)
-- Name: tipo_factura_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE tipo_factura_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tipo_factura_id_seq OWNER TO facturaronline;

--
-- TOC entry 2323 (class 0 OID 0)
-- Dependencies: 202
-- Name: tipo_factura_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE tipo_factura_id_seq OWNED BY tipo_factura.id;


--
-- TOC entry 203 (class 1259 OID 19800)
-- Name: usuario; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE usuario (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    apellido character varying(255),
    clave character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    nombre_o_razon_social character varying(255),
    telefono character varying(255),
    activacion character varying(255)
);


ALTER TABLE usuario OWNER TO facturaronline;

--
-- TOC entry 204 (class 1259 OID 19806)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE usuario_id_seq OWNER TO facturaronline;

--
-- TOC entry 2324 (class 0 OID 0)
-- Dependencies: 204
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;


--
-- TOC entry 205 (class 1259 OID 19986)
-- Name: usuario_rol; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE usuario_rol (
    usuario_id integer NOT NULL,
    rol_id integer NOT NULL,
    borrado boolean DEFAULT false
);


ALTER TABLE usuario_rol OWNER TO facturaronline;

--
-- TOC entry 2325 (class 0 OID 0)
-- Dependencies: 205
-- Name: TABLE usuario_rol; Type: COMMENT; Schema: public; Owner: facturaronline
--

COMMENT ON TABLE usuario_rol IS 'Es la relacion Many-To-Many entre los usuarios y los roles';


--
-- TOC entry 2029 (class 2604 OID 20067)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY banco ALTER COLUMN id SET DEFAULT nextval('banco_id_seq'::regclass);


--
-- TOC entry 2030 (class 2604 OID 20068)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY caja_chica ALTER COLUMN id SET DEFAULT nextval('caja_chica_id_seq'::regclass);


--
-- TOC entry 2031 (class 2604 OID 20069)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY carga_hs ALTER COLUMN id SET DEFAULT nextval('carga_hs_id_seq'::regclass);


--
-- TOC entry 2046 (class 2604 OID 20070)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cliente ALTER COLUMN id SET DEFAULT nextval('cliente_id_seq'::regclass);


--
-- TOC entry 2032 (class 2604 OID 20071)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cobro_alternativo ALTER COLUMN id SET DEFAULT nextval('cobro_alternativo_id_seq'::regclass);


--
-- TOC entry 2033 (class 2604 OID 20072)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cuenta_bancaria ALTER COLUMN id SET DEFAULT nextval('cuenta_bancaria_id_seq'::regclass);


--
-- TOC entry 2034 (class 2604 OID 20073)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY estado_factura_compra ALTER COLUMN id SET DEFAULT nextval('estado_factura_compra_id_seq'::regclass);


--
-- TOC entry 2047 (class 2604 OID 20074)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY estado_solicitud_pago ALTER COLUMN id SET DEFAULT nextval('estado_solicitud_pago_id_seq'::regclass);


--
-- TOC entry 2035 (class 2604 OID 20075)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY evento ALTER COLUMN id SET DEFAULT nextval('evento_id_seq'::regclass);


--
-- TOC entry 2036 (class 2604 OID 20076)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_compra ALTER COLUMN id SET DEFAULT nextval('factura_compra_id_seq'::regclass);


--
-- TOC entry 2048 (class 2604 OID 20077)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_solicitud_pago ALTER COLUMN id SET DEFAULT nextval('factura_solicitud_pago_id_seq'::regclass);


--
-- TOC entry 2037 (class 2604 OID 20078)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY modo_pago ALTER COLUMN id SET DEFAULT nextval('modo_pago_id_seq'::regclass);


--
-- TOC entry 2038 (class 2604 OID 20079)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY orden_pago ALTER COLUMN id SET DEFAULT nextval('orden_pago_id_seq'::regclass);


--
-- TOC entry 2039 (class 2604 OID 20080)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago ALTER COLUMN id SET DEFAULT nextval('pago_id_seq'::regclass);


--
-- TOC entry 2050 (class 2604 OID 20081)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago_solicitud_pago ALTER COLUMN id SET DEFAULT nextval('pago_solicitud_pago_id_seq'::regclass);


--
-- TOC entry 2040 (class 2604 OID 20082)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY proveedor ALTER COLUMN id SET DEFAULT nextval('proveedor_id_seq'::regclass);


--
-- TOC entry 2041 (class 2604 OID 20083)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY rol ALTER COLUMN id SET DEFAULT nextval('rol_id_seq'::regclass);


--
-- TOC entry 2049 (class 2604 OID 20084)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY solicitud_pago ALTER COLUMN id SET DEFAULT nextval('solicitud_pago_id_seq'::regclass);


--
-- TOC entry 2042 (class 2604 OID 20085)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY tipo_cuenta ALTER COLUMN id SET DEFAULT nextval('tipo_cuenta_id_seq'::regclass);


--
-- TOC entry 2043 (class 2604 OID 20086)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY tipo_factura ALTER COLUMN id SET DEFAULT nextval('tipo_factura_id_seq'::regclass);


--
-- TOC entry 2044 (class 2604 OID 20087)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);


--
-- TOC entry 2242 (class 0 OID 19671)
-- Dependencies: 172
-- Data for Name: banco; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO banco (id, borrado, nombre) VALUES (3, false, 'CitiBank');
INSERT INTO banco (id, borrado, nombre) VALUES (1, false, 'Santander Rio');
INSERT INTO banco (id, borrado, nombre) VALUES (2, false, 'Ciudad');
INSERT INTO banco (id, borrado, nombre) VALUES (4, true, 'Galicia');
INSERT INTO banco (id, borrado, nombre) VALUES (5, false, 'Nación');
INSERT INTO banco (id, borrado, nombre) VALUES (8, false, 'HSBC');
INSERT INTO banco (id, borrado, nombre) VALUES (7, true, 'Francés');
INSERT INTO banco (id, borrado, nombre) VALUES (6, true, 'Provincia');


--
-- TOC entry 2326 (class 0 OID 0)
-- Dependencies: 173
-- Name: banco_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('banco_id_seq', 8, true);


--
-- TOC entry 2244 (class 0 OID 19676)
-- Dependencies: 174
-- Data for Name: caja_chica; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (14, false, '2016-01-13 00:00:00', 'Yoli', '2016-01-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (15, false, '2016-01-15 00:00:00', 'Yoli', '2016-01-01', 250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (16, false, '2016-01-19 00:00:00', 'Farmacia', '2016-01-01', 115, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (17, false, '2016-01-20 00:00:00', 'Fichas', '2016-01-01', 80, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (25, false, '2016-01-29 00:00:00', 'Yoli', '2016-01-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (28, false, '2016-02-02 00:00:00', 'galletitas', '2016-02-01', 30, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (30, false, '2016-02-03 00:00:00', 'Yoli', '2016-02-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (32, false, '2016-02-05 00:00:00', 'facturas', '2016-02-01', 100, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (34, false, '2016-02-05 00:00:00', 'Expensas', '2016-02-01', 190, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (35, false, '2016-02-05 00:00:00', 'Yoli', '2016-02-01', 300, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (41, false, '2016-02-19 00:00:00', 'desayunos personal', '2016-02-01', 195, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (45, false, '2016-02-15 00:00:00', 'galletitas', '2016-02-01', 200, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (51, false, '2016-01-04 00:00:00', 'Taxi', '2016-02-01', 230, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (55, false, '2016-01-06 00:00:00', 'Expensas', '2016-02-01', 190, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (67, false, '2016-01-19 00:00:00', 'Alquiler', '2016-02-01', 4300, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (73, false, '2016-02-24 00:00:00', 'yoli 3 semanas', '2016-02-01', 1550, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (75, false, '2016-02-25 00:00:00', 'Facturas', '2016-02-01', 156, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (76, false, '2016-02-26 00:00:00', 'Papel higienico', '2016-02-01', 39, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (82, false, '2016-02-26 00:00:00', 'Yoli', '2016-02-01', 260, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (83, false, '2016-03-01 00:00:00', 'Fichas', '2016-03-01', 250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (84, false, '2016-03-02 00:00:00', 'Comida oficina', '2016-03-01', 175, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (85, false, '2016-03-02 00:00:00', 'Bidones', '2016-03-01', 275, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (87, false, '2016-03-02 00:00:00', 'Yoli', '2016-03-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (88, false, '2016-03-04 00:00:00', 'Yoli', '2016-03-01', 250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (91, false, '2016-03-08 00:00:00', 'Yoli (3 semanas)', '2016-03-01', 1250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (92, false, '2016-03-02 00:00:00', 'papel hig. y servilletas', '2016-03-01', 300, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (93, false, '2016-03-08 00:00:00', 'galletitas', '2016-03-01', 60, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (94, false, '2016-03-09 00:00:00', 'Yoli', '2016-03-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (98, false, '2016-03-11 00:00:00', 'Medialunas', '2016-03-01', 82, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (99, false, '2016-03-11 00:00:00', 'Yoli', '2016-03-01', 250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (101, false, '2016-03-14 00:00:00', 'galletitas', '2016-03-01', 25, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (105, false, '2016-03-16 00:00:00', 'azucar', '2016-03-01', 30, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (106, false, '2016-03-16 00:00:00', 'galletitas', '2016-03-01', 40, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (107, false, '2016-03-16 00:00:00', 'Yoli', '2016-03-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (123, false, '2016-03-29 00:00:00', 'Almacen', '2016-03-01', 197, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (111, false, '2016-03-18 00:00:00', 'Yoli', '2016-03-01', 280, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (116, false, '2016-03-23 00:00:00', 'Yoli', '2016-03-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (124, false, '2016-03-28 00:00:00', 'Libreria', '2016-03-01', 195, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (125, false, '2016-03-30 00:00:00', 'Yoli', '2016-03-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (126, false, '2016-03-30 00:00:00', 'Almacen', '2016-03-01', 354.8, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (129, false, '2016-03-29 00:00:00', 'Merceria', '2016-03-01', 156, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (130, false, '2016-03-29 00:00:00', 'Ferreteria', '2016-03-01', 125, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (132, false, '2016-03-29 00:00:00', 'Bazar', '2016-03-01', 220, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (133, false, '2016-04-01 00:00:00', 'Yoli', '2016-04-01', 250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (134, false, '2016-03-22 00:00:00', 'Expensas', '2016-04-01', 190, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (135, false, '2016-04-01 00:00:00', 'fichas', '2016-04-01', 200, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (136, false, '2016-04-05 00:00:00', 'galletitas', '2016-04-01', 100, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (138, false, '2016-04-15 00:00:00', 'Almacen', '2016-04-01', 402, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (141, false, '2016-04-08 00:00:00', 'yoli', '2016-04-01', 250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (142, false, '2016-04-06 00:00:00', 'Agua', '2016-04-01', 329.99, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (143, false, '2016-04-13 00:00:00', 'yoli', '2016-04-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (144, false, '2016-04-06 00:00:00', 'yoli', '2016-04-01', 350, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (146, false, '2016-04-15 00:00:00', 'Yoli', '2016-04-01', 250, false, true, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (1, false, '2016-01-20 00:00:00', 'Yoli', '2016-01-01', 350, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (2, false, '2016-01-22 00:00:00', 'Yoli', '2016-01-01', 250, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (3, false, '2016-01-26 00:00:00', 'Yoli', '2016-01-01', 300, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (4, false, '2016-01-27 00:00:00', 'Aguas', '2016-01-01', 275, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (8, false, '2016-01-27 00:00:00', 'Yoli', '2016-01-01', 350, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (10, false, '2016-01-28 00:00:00', 'galletitas', '2016-01-01', 93, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (12, false, '2016-01-06 00:00:00', 'Yoli', '2016-01-01', 350, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (13, false, '2016-01-08 00:00:00', 'Yoli', '2016-01-01', 250, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (37, false, '2016-02-12 00:00:00', 'Moto', '2016-02-01', 130, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (38, false, '2016-02-11 00:00:00', 'varios', '2016-02-01', 1880, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (52, false, '2016-01-04 00:00:00', 'Libreria', '2016-02-01', 52, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (53, false, '2016-01-04 00:00:00', 'Libreria', '2016-02-01', 135, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (54, false, '2016-01-04 00:00:00', 'Santander Rio', '2016-02-01', 500, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (56, false, '2016-01-06 00:00:00', 'OCA', '2016-02-01', 180, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (58, false, '2016-01-06 00:00:00', 'Papelería', '2016-02-01', 290, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (61, false, '2016-01-11 00:00:00', 'Grafica', '2016-02-01', 19.96, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (62, false, '2016-01-11 00:00:00', 'ferretería', '2016-02-01', 125, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (64, false, '2016-01-19 00:00:00', 'Grafica', '2016-02-01', 90.75, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (65, false, '2016-01-19 00:00:00', 'Ferreteria', '2016-02-01', 132, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (66, false, '2016-01-19 00:00:00', 'Comida oficina', '2016-02-01', 170, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (95, false, '2016-03-10 00:00:00', 'Autoservicio', '2016-03-01', 219, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (96, false, '2016-03-10 00:00:00', 'Almacen', '2016-03-01', 221, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (118, false, '2016-03-23 00:00:00', 'Libreria', '2016-03-01', 600, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (117, false, '2016-03-23 00:00:00', 'Autoservicio', '2016-03-01', 128.9, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (131, false, '2016-03-29 00:00:00', 'Libreria', '2016-03-01', 186, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (139, false, '2016-04-15 00:00:00', 'Ferreteria', '2016-04-01', 214, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (140, false, '2016-04-05 00:00:00', 'Grafica', '2016-04-01', 7, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (145, false, '2016-04-28 00:00:00', 'ferreteria', '2016-03-01', 48, true, false, NULL);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (6, false, '2016-01-26 00:00:00', 'Cafe Paul y Wanda', '2016-01-01', 80, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (7, false, '2016-01-26 00:00:00', 'Dagotto', '2016-01-01', 441.65, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (9, false, '2016-01-28 00:00:00', 'Sistema', '2016-01-01', 5200, false, false, 2);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (11, false, '2016-01-21 00:00:00', 'libreria', '2016-01-01', 410, false, false, 3);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (18, false, '2016-01-18 00:00:00', 'Paul', '2016-01-01', 300, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (19, false, '2016-01-21 00:00:00', 'Intercolor', '2016-01-01', 36.1, false, false, 18);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (20, false, '2016-01-28 00:00:00', 'Portaretrato', '2016-01-01', 360, false, false, 17);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (21, false, '2016-01-28 00:00:00', 'Ploteo', '2016-01-01', 25, false, false, 12);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (22, false, '2016-01-12 00:00:00', 'Cablevision Juanchi', '2016-01-01', 512.4, false, false, 3);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (23, false, '2016-01-12 00:00:00', 'Paul', '2016-01-01', 300, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (26, false, '2016-02-01 00:00:00', 'Libreria', '2016-02-01', 95, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (27, false, '2016-02-01 00:00:00', 'Ferreteria', '2016-02-01', 80, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (29, false, '2016-02-03 00:00:00', 'Ferreteria', '2016-02-01', 87, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (31, false, '2016-02-04 00:00:00', 'Libreria', '2016-02-01', 1070.05, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (33, false, '2016-02-04 00:00:00', 'Papelera', '2016-02-01', 30, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (36, false, '2016-02-10 00:00:00', 'Papelera', '2016-02-01', 124, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (39, false, '2016-02-22 00:00:00', 'Servidor', '2016-02-01', 300, false, false, 2);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (40, false, '2016-02-22 00:00:00', 'almuerzo', '2016-02-01', 100, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (42, false, '2016-02-19 00:00:00', 'desayuno', '2016-02-01', 84, false, false, 17);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (43, false, '2016-02-22 00:00:00', 'Celular', '2016-02-01', 300, false, false, 18);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (44, false, '2016-02-22 00:00:00', 'comidas', '2016-02-01', 600, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (46, false, '2016-02-17 00:00:00', 'Almuerzo', '2016-02-01', 280, false, false, 18);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (47, false, '2016-02-17 00:00:00', 'Easy color', '2016-02-01', 528, false, false, 18);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (48, false, '2016-02-17 00:00:00', 'Vivero', '2016-02-01', 270, false, false, 18);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (49, false, '2016-02-16 00:00:00', 'Forum', '2016-02-01', 1100, false, false, 6);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (50, false, '2016-01-04 00:00:00', 'Monotributo Paul', '2016-02-01', 200, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (57, false, '2016-01-06 00:00:00', 'Fotografía', '2016-02-01', 60.15, false, false, 17);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (68, false, '2016-01-21 00:00:00', 'Remis', '2016-02-01', 400, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (69, false, '2016-01-21 00:00:00', 'Fichas', '2016-02-01', 100, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (70, false, '2016-02-24 00:00:00', 'Fichas Paul', '2016-02-01', 20, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (71, false, '2016-02-24 00:00:00', 'Farmacia', '2016-02-01', 78.25, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (74, false, '2016-02-25 00:00:00', 'Fichas', '2016-02-01', 28, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (77, false, '2016-02-26 00:00:00', 'Papelera', '2016-02-01', 120, false, false, 12);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (78, false, '2016-02-26 00:00:00', 'Particular', '2016-02-01', 200, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (79, false, '2016-02-24 00:00:00', 'Particular', '2016-02-01', 200, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (80, false, '2016-03-01 00:00:00', 'Particular', '2016-03-01', 50, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (81, false, '2016-03-01 00:00:00', 'Cerrajería', '2016-03-01', 45, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (86, false, '2016-03-03 00:00:00', 'Ferretería', '2016-03-01', 324.97, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (89, false, '2016-03-08 00:00:00', 'Ferreteria', '2016-03-01', 550, false, false, 17);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (90, false, '2016-03-08 00:00:00', 'Ferreteria', '2016-03-01', 825, false, false, 17);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (97, false, '2016-03-07 00:00:00', 'Personal', '2016-03-01', 200, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (100, false, '2016-03-14 00:00:00', 'Tramite Juanchi', '2016-03-01', 320, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (102, false, '2016-03-14 00:00:00', 'estacionamiento', '2016-03-01', 140, false, false, 18);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (103, false, '2016-03-15 00:00:00', 'particular', '2016-03-01', 100, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (104, false, '2016-03-16 00:00:00', 'Etiquetas', '2016-03-01', 400, false, false, 17);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (108, false, '2016-03-16 00:00:00', 'papeleria', '2016-03-01', 145, false, false, 17);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (109, false, '2016-03-17 00:00:00', 'Taller', '2016-03-01', 480, false, false, 6);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (110, false, '2016-03-18 00:00:00', 'Monotributo feb - mar', '2016-03-01', 4720, false, false, 3);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (114, false, '2016-03-23 00:00:00', 'estacionamiento', '2016-03-01', 300, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (115, false, '2016-03-18 00:00:00', 'fichas', '2016-03-01', 200, false, false, 15);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (119, false, '2016-03-28 00:00:00', 'Libreria', '2016-03-01', 95, false, false, 3);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (120, false, '2016-03-28 00:00:00', 'Ferretería', '2016-03-01', 156, false, false, 2);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (121, false, '2016-03-28 00:00:00', 'Ferretería', '2016-03-01', 193, false, false, 2);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (122, false, '2016-03-28 00:00:00', 'Libreria', '2016-03-01', 446.01, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (112, false, '2016-03-17 00:00:00', 'Libreria', '2016-03-01', 560.71, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (113, false, '2016-03-21 00:00:00', 'Aromatizacion', '2016-03-01', 127.05, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (127, false, '2016-03-28 00:00:00', 'Madereria', '2016-03-01', 156, false, false, 2);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (128, false, '2016-03-28 00:00:00', 'Madereria', '2016-03-01', 193, false, false, 2);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (137, false, '2016-04-03 00:00:00', 'celular', '2016-04-01', 430, false, false, 2);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (147, false, '2016-04-18 00:00:00', 'Aromatización', '2016-04-01', 127.05, false, false, 16);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (5, false, '2016-01-26 00:00:00', 'Cocheras Juanchi/Chris', '2016-01-01', 8350, false, false, 13);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (24, false, '2016-01-29 00:00:00', 'retiro', '2016-01-01', 1000, false, false, 13);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (59, false, '2016-01-08 00:00:00', 'Telecom personal', '2016-02-01', 500, false, false, 13);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (60, false, '2016-01-08 00:00:00', 'Telecom personal', '2016-02-01', 1500, false, false, 13);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (63, false, '2016-01-13 00:00:00', 'Telecom personal', '2016-02-01', 500, false, false, 13);
INSERT INTO caja_chica (id, borrado, fecha, detalle, mes, monto, solicitado_por_otros, solicitado_por_todos, usuario_solicitante_id) VALUES (72, false, '2016-02-24 00:00:00', 'Fotografia y cuadro', '2016-02-01', 185, false, false, 13);


--
-- TOC entry 2327 (class 0 OID 0)
-- Dependencies: 175
-- Name: caja_chica_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('caja_chica_id_seq', 147, true);


--
-- TOC entry 2246 (class 0 OID 19684)
-- Dependencies: 176
-- Data for Name: carga_hs; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (53, false, 'Armado de idea de creacion de Solicitud de Pago', '2016-03-15 09:08:02.22', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (54, false, 'modificacion menu nuevo', '2016-03-16 09:38:23.633', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (3, false, 'Pagina de Carga de Horas', '2015-09-26 02:06:49.778', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (4, true, 'borrar', '2015-09-26 02:06:57.84', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (55, false, 'diseño db solicitud pago', '2016-03-18 13:41:39.063', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (2, false, 'Pagina Login', '2015-09-24 02:04:37.946', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (5, false, 'Fin Pagina carga hs', '2015-09-27 00:23:28.887', 0.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (56, false, 'creacion tablas db', '2016-03-18 13:41:46.23', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (57, false, 'cambio factura a facturaCompras en todos lados, desde la db', '2016-03-18 13:41:53.406', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (58, false, 'cambio de paquetes y nombres en todos lados, todo lo ya hecho ahora pertenece a compras', '2016-03-18 13:42:57.61', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (7, false, 'Arreglos varios y pagina Bancos', '2015-09-28 09:41:35.353', 1.3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (8, false, 'Tablas fijas: Modo de Pago, Tipo Cuenta, Estados de Factura', '2015-10-02 10:15:56.894', 1.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (59, false, 'modificaciones extras pedidas por angel', '2016-03-20 00:28:56.847', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (60, false, 'OPs normales y cpysf ahora tienen listado de transferencias', '2016-03-20 00:29:23.329', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (61, false, 'comienzo desarrollo seccion de solicitud de pago', '2016-03-20 22:13:36.638', 5.4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (6, true, 'borrar', '2015-09-27 00:24:47.238', 12);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (62, false, 'agregado de columnas en listado solicitud pago', '2016-03-21 14:24:37.751', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (1, false, 'Investigacion inicial / pensar idea / arquitectura', '2015-09-22 02:04:19.193', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (10, false, 'continuacion Proveedores', '2015-10-04 21:44:16.901', 8);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (9, true, 'Inicio de armado de Proveedores', '2015-10-02 10:16:03.408', 3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (11, false, 'proveedores: ppio cobros alternativos y demas', '2015-10-28 01:26:13.053', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (12, false, 'desarrollo de Cobros Alternativos', '2015-10-28 13:28:39.965', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (13, false, 'fin cobros alternativos + bug fixing', '2015-10-29 00:59:33.501', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (14, false, 'validaciones varias', '2015-10-29 18:14:40.678', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (15, false, 'validaciones carga proveedores', '2015-10-30 01:49:41.42', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (16, false, 'beautifications', '2015-10-30 13:53:57.905', 0.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (17, false, 'inicio facturas', '2015-11-01 23:49:27.46', 6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (18, false, 'continuacion facturas', '2015-11-02 01:33:13.409', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (19, false, 'continuacion facturas 2', '2015-11-02 12:15:49.101', 3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (20, false, 'continuacion facturas 3', '2015-11-03 19:27:24.885', 1.6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (21, false, 'validaciones carga facturas', '2015-11-03 22:38:49.548', 2.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (22, false, 'fin facturas', '2015-11-04 01:49:12.272', 1.75);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (23, false, 'comienzo desarrollo ordenes de pago', '2015-11-05 13:12:22.581', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (24, false, 'inicio idea ordenes de pago', '2015-11-08 17:08:13.002', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (25, false, 'inicio desarrollo Idea de Ordenes de Pago', '2015-11-09 12:17:17.85', 3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (26, false, 'arreglos varios Ordenes de Pago', '2015-11-09 22:40:18.993', 1.25);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (27, false, 'intento de mostrar importes con mismo formato', '2015-11-11 03:22:14.524', 2.55);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (28, false, 'TODOS los importes van con dos decimales, si es entero agregar (.00)', '2015-11-12 01:45:53.287', 0.75);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (29, false, 'comienzo desarrollo del 3er paso para op normal', '2015-11-12 12:14:19.917', 2.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (30, false, 'continuacion desarrollo del 3er paso para op normal', '2015-11-17 12:50:21.255', 13.3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (31, false, 'arreglos varios Ordenes de Pago Normal', '2015-11-17 19:28:40.396', 2.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (32, false, 'inicio 4to paso carga OP Normal', '2015-11-18 09:22:45.459', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (33, false, 'continuacion 4to paso y arreglos para persistencia de OP Normal', '2015-11-18 17:31:08.563', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (34, false, 'Agregue concepto, evento, solicitado por a carga de OP Normal', '2015-11-18 18:19:39.464', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (35, false, 'desarrollo ult pagina de OP Normal', '2015-11-19 10:55:00.433', 2.1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (36, false, 'Varias validaciones carga OP Normal', '2015-11-20 12:08:03.187', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (37, false, 'Varias validaciones carga OP Normal 2', '2015-11-20 19:52:43.472', 0.7);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (38, false, 'mas validaciones carga OP Normal , y armado de visibilidades de Modos Pago segun corresponde', '2015-11-23 10:26:35.644', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (39, false, 'Ahora al crear una nueva OP , se guarda el estado de la factura Cancelada o Pagada parcial segun corresponda', '2015-11-23 12:25:31.57', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (40, false, 'Ahora se calculan los importes netos (por si es una fact pagada parcial), tb agregue btn volver en la ultima pag de creacion de OP normal', '2015-11-23 16:53:00.742', 1.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (41, false, 'Arreglo de Many-to-Many. Tb implementacion de Delete de OPs, y arreglos para carga de OPs Normales', '2015-11-25 10:41:16.39', 8.4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (42, false, 'Implementacion de creacion de OPs CP y SF , OPs SP y SF, arreglos de los filtros de listado de OPs', '2015-11-26 11:30:34.843', 6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (43, false, 'agregado de los nuevos tipos de ModoPago Tarjeta Credito y Transf para OP_SPySF. Tb arreglos varios', '2015-11-26 22:25:17.469', 5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (44, false, 'No permito editar facturas ni OP -> Pero sí dejo "ver detalles"', '2015-11-30 15:43:40.485', 2.3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (45, false, 'comienzo de armado de reportes (excel), y arreglos varios', '2015-12-09 10:16:50.926', 15.6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (46, false, 'ahora el excel de op se genera en los 3 tipos de ops . tb arregle bug de listado de OPs en el paginado y los filtros', '2015-12-09 15:45:20.081', 2.6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (47, false, 'export de listado facturas', '2015-12-10 01:07:01.034', 0.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (48, false, 'Agregado de MesImpositivo a la factura, y desarrollo de Reporte Subdiario', '2015-12-13 21:31:59.081', 6.6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (49, false, 'arreglos varios Menu y agregado de reporte Deudas vs Pagos', '2015-12-14 12:56:46.456', 3.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (50, false, 'Download Archivos', '2015-12-15 18:14:05.133', 3.6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (51, false, 'Detalles Finales', '2015-12-16 21:39:17.111', 3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (52, false, 'Arreglo BUG en el listado de OPs, ahora andan bien los filtros, paginados y se muestra ordenado por Nro OP descendente', '2015-12-17 12:07:24.427', 3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (67, false, 'Inicio de Crear Solicitud Pago Normal', '2016-04-05 13:00:11.444', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (63, false, 'agregado de columnas y filtros en listado solicitudes de pago, y modificaciones varias', '2016-03-22 12:04:52.24', 7);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (64, false, 'Inicio de cambio de roles: ahora va a ser many-to-many', '2016-03-31 15:02:53.253', 16);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (65, false, 'Continuacion de cambio de roles: ahora es many-to-many', '2016-04-01 14:46:11.399', 6);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (66, false, 'chau solicitantes - hola usuarios con rol solicitante . Nuevo seteo de visibilidades . Arreglos Varios', '2016-04-04 13:22:47.091', 12.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (70, false, 'Finalizacion de pagina para crear sol de pago normal. Comienzo de creacion de sol cpysf', '2016-04-09 19:06:49.804', 1.7);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (71, false, 'Fin creacion sol de pago cpysf y spysf', '2016-04-10 23:28:07.807', 3.4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (68, false, 'Continuacion Crear Solicitud Pago Normal', '2016-04-06 15:00:27.446', 9.7);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (69, false, 'validaciones al intentar crear solicitud de pago', '2016-04-08 15:32:55.136', 7.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (72, false, 'Inicio Ver Detalles Solicitudes de pago', '2016-04-12 12:24:48.941', 3.1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (73, false, 'Listo el Rechazar y el Eliminar para Solicitudes de Pago', '2016-04-12 15:31:33.494', 2.7);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (74, false, 'Conversion a OP: Ya anda el crear prov, y comence el crear fact', '2016-04-13 15:13:40.44', 7.9);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (75, false, 'Conversion a OP: ya anda el crear factura', '2016-04-14 10:12:51.812', 1.9);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (76, false, 'Conversion a OP: comienzo de crear op', '2016-04-15 18:30:23.37', 6.7);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (77, false, 'Conversion a OP: ya andan cpysf y spysf', '2016-04-16 02:23:37.057', 4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (78, false, 'Conversion a OP: Comienzo sol normal, y arreglos varios cpysf y spysf', '2016-04-16 17:45:23.812', 1.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (79, false, 'Conversion a OP: ya anda OP normal tb. Ultime varios detalles pendientes tb.', '2016-04-18 18:29:35.165', 9.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (80, false, 'Calculos de iva segun tipo factura, y se saco TipoCuenta, NroCuenta, Banco de spysf', '2016-04-19 17:56:37.028', 3.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (81, false, 'Reporte "Totales por Evento, valido nroFactVenta (0003-0000xxx). Tb Eventos ahora tienen nueva col "Mostrar a solicitantes"', '2016-04-20 16:59:25.301', 4.4);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (82, false, 'Chau sistema y plan de todos lados, para que ande todo mas rapido', '2016-04-21 10:03:38.689', 1.3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (83, false, 'Listados, por defecto no muestran el listado, se muestran con uso de filtros', '2016-04-21 15:19:01.318', 2.3);


--
-- TOC entry 2328 (class 0 OID 0)
-- Dependencies: 177
-- Name: carga_hs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('carga_hs_id_seq', 83, true);


--
-- TOC entry 2276 (class 0 OID 20002)
-- Dependencies: 206
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (10, false, 'FINNING SOLUCIONES MINERAS S.A.', '30-68153032-7', 'Estanislao Zeballos 2739 - Beccar - Bs.As.', 0, '5030-8611', NULL, 'Rodrigo Aguilar', 'Rodrigo.aguilar@finning.com.ar', NULL, NULL, 0, 0, 0, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (9, false, 'FINNING ARGENTINA S.A.', '30-64722711-9', 'Estanislao Zeballos 2739 - Beccar - Bs.As.', 0, '5030-8611', NULL, 'Rodrigo Aguilar', 'Rodrigo.aguilar@finning.com.ar', NULL, NULL, 0, 0, 0, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (8, false, 'ESCANDINAVIA DEL PLATA S.A.', '30-70924647-6', 'Panamericana Km. 33.600 - Grand Bour - Bs.As.', 0, '0332-7415658', '15 -5423 8276', 'Gonzalo', 'gonzalo.onorato@volvo.com', 'Martes y Jueves de 10.00 a 12.00 y 15.00 a 17.00 hs.', NULL, 0, 30, 15, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (7, false, 'BUMERAN.COM ARGENTINA S.A.', '30-70605381-2', 'Cabrera Jose 5354 - CABA', 0, '4852-7400', NULL, 'Sr. Camilo', 'proveedores@navent.com', NULL, NULL, 0, 30, 15, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (6, false, 'BUHL SOCIEDAD ANONIMA', '30-63780514-9', 'Uruguay 485 piso 10 - CABA', 0, '4724-1700', NULL, 'Gisela / Angelina', 'cfiorillo@buhlsa.com', 'RETIRAR PAGO : Juan de Garay 235 Villa Lynch - San Martin 15.30 a 17.30', NULL, 0, 30, 15, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (4, false, 'BASF ARGENTINA S.A.', '30-51748667-8', 'Tucuman 1 - CABA', 0, NULL, NULL, NULL, 'ingresofacturas-ar@basf.com', NULL, NULL, 30, 5, 5, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (3, false, 'AON Benfield Argentina', '30-71048832-7', 'Emma de la Barra 353 piso 4 - CABA', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 30, 15, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (2, false, 'ALCON LABORATORIOS ARGENTINA S.A.', '30-51684266-7', 'A.Moreau de Justo 240 piso 2 - CABA', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 15, 30, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (1, false, 'AEROPUERTOS ARGENTINA 2000 S.A.', '30-69617058-0', 'Suipacha 268  Honduras 5673 - ( 1008 ) - CABA', 0, '4576-5328', '4852-6553', 'Avila Alejandra', NULL, 'ALEJANDRA O VERONICA - LLAMAR DE 9 A 13 Y 15 A 18 HS ', NULL, 0, 15, 30, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (5, false, 'BIOGENESIS BAGO S.A.', '30-65339981-9', 'Panamericana Km 38,5 - Garin - Bs. As.', 0, NULL, NULL, NULL, 'consultaproveedores@biogenesisbago.com', NULL, NULL, 0, 30, 15, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (17, false, '3M ARGENTINA SACIFIA', '30-52999439-3', 'Colectora Oeste de Panamericana 576 - Garín - Bs.As.', 0, '(0348)-4659514', '15-4494-9380', 'Maria Luz Benchuga', 'mlbenchuga@mmm.com', 'RETIRAR PAGO : Banco ICBC (ex Standard Bank) Bme. Mitre 744 - CABA', NULL, 0, 15, 30, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (16, false, 'Servicios y Productos para bebidas refrescante S.R.L.', '30-67773730-8', 'Paraguay 733 - CABA', 0, '4319-2300', NULL, 'Micaela Olmos Roddriguez', 'molmosrodriguez@coca-cola.com', NULL, NULL, 0, 15, 30, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (15, false, 'REMAX ARGENTINA S.R.L.', '33-70892523-9', 'Av. Callao 1515 - piso 1 - depto B - CABA', 0, '5235-7362', NULL, 'Roberto Ferreyra', 'roberto@remax.com.ar', NULL, NULL, 0, 30, 15, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (14, false, 'REDES Y DISTRIBUCION S.A.', '30-70790057-8', 'Cuba 2777 - CABA', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 30, 15, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (13, false, 'OBRA SOCIAL A.S.E.', '30-57807999-4', 'Lima 87 - piso 8 - CABA', 0, '4124-2800  (int.3866)', NULL, 'Hernán Bernal', NULL, 'Ariana Barrios', NULL, 0, 15, 30, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (12, false, 'MEDIFE AC', '30-68273765-0', 'Av. Juan B. Alberdi 3541 - CABA', 0, '4630-2700', NULL, NULL, NULL, 'de 15 a 18 hs.', NULL, 0, 15, 30, 0);
INSERT INTO cliente (id, borrado, razon_social, cuit_cuil, domicilio, mascara_modo_cobros, telefono1, telefono2, contacto, mails, referencias, observaciones, porcentaje_directivo_bianca, porcentaje_directivo_paul, porcentaje_directivo_cristian, porcentaje_directivo_juan) VALUES (11, false, 'GOBIERNO DE LA CIUDAD AUTONOMA DE BS. AS.', '34-99903208-9', 'Av. Rivadavia 524 - piso 2 - CABA', 0, '4320-5700', '4342-5052 int.134/135', NULL, NULL, NULL, NULL, 0, 15, 30, 0);


--
-- TOC entry 2329 (class 0 OID 0)
-- Dependencies: 207
-- Name: cliente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('cliente_id_seq', 17, true);


--
-- TOC entry 2248 (class 0 OID 19692)
-- Dependencies: 178
-- Data for Name: cobro_alternativo; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO cobro_alternativo (id, borrado, titular, cuit_cuil, cuenta_bancaria_id, proveedor_id) VALUES (1, true, 'Mutual Aprepa', '30-60107990-5', 65, 89);


--
-- TOC entry 2330 (class 0 OID 0)
-- Dependencies: 179
-- Name: cobro_alternativo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('cobro_alternativo_id_seq', 1, true);


--
-- TOC entry 2250 (class 0 OID 19697)
-- Dependencies: 180
-- Data for Name: cuenta_bancaria; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (1, false, NULL, NULL, NULL, '0167777100001901894491');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (2, false, NULL, NULL, NULL, '0070235720000001359853');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (3, false, NULL, NULL, NULL, '0720087820000000359304');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (4, false, NULL, NULL, NULL, '0720321120000000504872');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (5, false, NULL, NULL, NULL, '0150925402000101320500');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (6, false, NULL, NULL, NULL, '0070999020000052497944');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (7, false, NULL, NULL, NULL, '0200424611000030216758');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (8, false, NULL, '10139/0', 1, '0720207220000001013904');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (9, false, NULL, NULL, NULL, '0167777100002088935216');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (10, false, NULL, NULL, NULL, '0720005220000001353268');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (11, false, NULL, NULL, NULL, '0070372520000000409566');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (12, false, NULL, NULL, NULL, '1500625900062560590982');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (13, false, NULL, NULL, NULL, '0110458930045802655215');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (14, false, NULL, NULL, NULL, '0110521630052111204115');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (15, false, NULL, NULL, NULL, '0270101710003729280011');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (16, false, NULL, '7133-8 039-4 SUC 039', NULL, '0070039920000007133846');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (17, false, NULL, NULL, NULL, '0340045600104806751017');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (18, false, NULL, NULL, NULL, '0270101710012946630024');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (19, false, NULL, NULL, NULL, '0200424611000030061358');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (20, false, NULL, NULL, NULL, '0200424601000000065965');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (21, false, NULL, NULL, NULL, '0720034220000000290922');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (22, false, NULL, NULL, NULL, '0270054010000429080013');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (23, false, NULL, NULL, NULL, '3300008210080036541059');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (24, false, NULL, NULL, NULL, '0720496220000000125886');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (25, false, NULL, NULL, NULL, '0070050430004019159019');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (26, false, NULL, NULL, NULL, '0720049620000000302322');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (27, false, NULL, NULL, NULL, '0720455988000035133540');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (28, false, NULL, NULL, NULL, '2850850230000000106105');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (29, false, NULL, NULL, NULL, '0070176730004018629531');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (30, false, NULL, NULL, NULL, '0170356420000030537712');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (31, false, NULL, NULL, NULL, '0720242388000036404540');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (32, false, NULL, NULL, NULL, '0070176720000003096640');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (33, false, NULL, NULL, NULL, '0170339740000062476793');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (34, false, NULL, NULL, NULL, '0720452820000000039486');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (35, false, NULL, NULL, NULL, '0720100088000036613290');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (36, false, NULL, NULL, NULL, '0110213230021392937315');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (37, false, NULL, NULL, NULL, '0170276920000000268299');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (38, false, NULL, NULL, NULL, '0070181120000003021839');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (39, false, NULL, NULL, NULL, '0150537502000000170570');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (40, false, NULL, NULL, NULL, '0070365730004001055312');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (41, false, NULL, NULL, NULL, '0170318240000072169298');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (42, false, NULL, NULL, NULL, '0150849701000113696001');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (43, false, NULL, NULL, NULL, '1910131855013101276078');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (44, false, NULL, NULL, NULL, '0720053388000037134718');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (45, false, NULL, NULL, NULL, '0070019120000010656217');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (46, false, NULL, NULL, NULL, '3300000620000785490083');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (47, false, NULL, NULL, NULL, '0110134430013411668115');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (48, false, NULL, NULL, NULL, '1910011755001100472450');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (49, false, NULL, NULL, NULL, '0170470320000000067841');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (50, false, NULL, NULL, NULL, '2590047920091477830146');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (51, false, NULL, NULL, NULL, '0070007820000014587700');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (52, false, NULL, NULL, NULL, '0170478920000000182371');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (53, false, NULL, NULL, NULL, '0720217188000035746290');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (54, false, NULL, NULL, NULL, '0290039110000000265125');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (55, false, NULL, NULL, NULL, '0070526430004001049900');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (56, false, NULL, NULL, NULL, '1500020600007032021728');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (57, false, NULL, NULL, NULL, '0150513901000124052036');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (58, false, NULL, NULL, NULL, '0070145330004005545169');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (59, false, NULL, NULL, NULL, '2850159430094092359411');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (60, false, NULL, NULL, NULL, '1910029255102901453403');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (61, false, NULL, NULL, NULL, '2850124230000240002315');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (62, false, NULL, NULL, NULL, '0150521402000103259105');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (63, false, NULL, NULL, NULL, '2850303340094796249918');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (66, false, NULL, NULL, NULL, '1500001500005100628332');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (67, false, NULL, NULL, NULL, '0720207288000036099072');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (65, true, NULL, NULL, NULL, '2850850230000000106105');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (64, true, NULL, NULL, NULL, '2850850230000000106105');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (68, false, NULL, NULL, NULL, '0720015120000002576804');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (69, false, 2, '4201040255', NULL, '0110420620042010402551');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (70, false, 2, '34715150026952', NULL, '0110515520051500269527');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (71, false, 1, '420111099-1', 5, '0110420630042011109915');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (72, false, NULL, NULL, 1, '0720218888000035494236');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (73, false, NULL, NULL, 1, '0720100020000050088940');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (74, false, NULL, NULL, NULL, '0170141420000004902030');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (75, false, NULL, '457-325955/3', NULL, '0720457388000032595536');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (76, false, 2, '540381618', NULL, '0070161320000005403884');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (77, false, NULL, NULL, NULL, '1910149355014900630964');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (78, false, NULL, NULL, 8, '1500018300006832402414');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (79, false, NULL, NULL, NULL, '0110600430060003081065');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (80, false, NULL, NULL, NULL, '0167777100052492684208');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (81, false, NULL, NULL, NULL, '2850540440036574142013');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (82, false, NULL, NULL, NULL, '0720217120000000070704');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (83, false, NULL, NULL, NULL, '0150512202000102071056');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (84, false, NULL, NULL, NULL, '0170216520000001118401');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (85, false, NULL, NULL, NULL, '2850867030094082969701');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (86, false, NULL, NULL, NULL, '0720077920000001079090');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (87, false, NULL, NULL, NULL, '1910052055005201925464');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (88, false, 2, NULL, NULL, '1500639600063932281778');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (89, false, 2, '301354-1', NULL, '0170326720000030135411');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (90, false, NULL, NULL, NULL, '0110655420065500129387');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (91, false, NULL, NULL, NULL, '0070368820000000794097');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (92, false, NULL, NULL, NULL, '0070092420000004140391');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (93, false, NULL, NULL, NULL, '0720176520000001054918');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (94, false, NULL, NULL, NULL, '0070083230004027882336');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (95, false, NULL, NULL, NULL, '0070325130004013719709');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (96, false, NULL, NULL, NULL, '1910060555006000217428');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (97, false, NULL, NULL, NULL, '2850320030000000162857');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (98, false, NULL, NULL, NULL, '1500054100030034721228');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (99, false, NULL, NULL, NULL, '0290049000000000083632');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (100, false, NULL, NULL, NULL, '0270042710001889430022');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (101, false, NULL, NULL, NULL, '0167777100002290496152');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (102, false, NULL, NULL, NULL, '2850310140094788094438');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (103, false, NULL, NULL, NULL, '0720093988000004037004');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (104, false, NULL, NULL, NULL, '0720204120000000225890');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (105, false, NULL, NULL, NULL, '0170239420000000115830');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (106, false, NULL, NULL, NULL, '2850684130094000041441');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (107, false, NULL, NULL, NULL, '0110652320065200304923');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (108, false, NULL, NULL, NULL, '0070339820000016681011');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (109, false, NULL, NULL, NULL, '0167777100055967610218');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (110, false, NULL, NULL, NULL, '0070236420000002545853');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (111, false, NULL, NULL, NULL, '0070313820000005980482');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (112, false, NULL, NULL, NULL, '0720191820000000541204');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (113, false, NULL, NULL, NULL, '0070019120000007846913');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (114, false, NULL, NULL, NULL, '0150511502000000931544');
INSERT INTO cuenta_bancaria (id, borrado, tipo_cuenta_id, nro_cuenta, banco_id, cbu) VALUES (115, false, NULL, NULL, NULL, '0070039920000007858200');


--
-- TOC entry 2331 (class 0 OID 0)
-- Dependencies: 181
-- Name: cuenta_bancaria_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('cuenta_bancaria_id_seq', 115, true);


--
-- TOC entry 2252 (class 0 OID 19702)
-- Dependencies: 182
-- Data for Name: estado_factura_compra; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO estado_factura_compra (id, borrado, nombre, descripcion) VALUES (1, false, 'Pendiente de Pago', 'Pendiente de Pago');
INSERT INTO estado_factura_compra (id, borrado, nombre, descripcion) VALUES (2, false, 'Cancelada', 'Cancelada');
INSERT INTO estado_factura_compra (id, borrado, nombre, descripcion) VALUES (3, false, 'Pagada Parcial', 'Pagada Parcial');


--
-- TOC entry 2332 (class 0 OID 0)
-- Dependencies: 183
-- Name: estado_factura_compra_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('estado_factura_compra_id_seq', 3, true);


--
-- TOC entry 2278 (class 0 OID 20013)
-- Dependencies: 208
-- Data for Name: estado_solicitud_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO estado_solicitud_pago (id, borrado, nombre, descripcion) VALUES (1, false, 'Pendiente (Crear Proveedor)', 'Caso en que no existe el proveedor y hay que crearlo, luego crear las facturas, y por ultimo crear la OP.');
INSERT INTO estado_solicitud_pago (id, borrado, nombre, descripcion) VALUES (2, false, 'Pendiente (Crear Factura/s)', 'Caso en que no existe/n la/s factura/s en el sistema. Luego hay que crear la OP tambien.');
INSERT INTO estado_solicitud_pago (id, borrado, nombre, descripcion) VALUES (3, false, 'Pendiente (Crear OP)', 'Caso en el que el proveedor y las facturas ya estan cargadas en el sistema. Solo falta crear la OP.');
INSERT INTO estado_solicitud_pago (id, borrado, nombre, descripcion) VALUES (4, false, 'Cumplida', 'La OP fue creada a partir de la solicitud de pago.');
INSERT INTO estado_solicitud_pago (id, borrado, nombre, descripcion) VALUES (5, false, 'Rechazada', 'Caso en que se rechaza la solicitud de pago.');


--
-- TOC entry 2333 (class 0 OID 0)
-- Dependencies: 209
-- Name: estado_solicitud_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('estado_solicitud_pago_id_seq', 5, true);


--
-- TOC entry 2254 (class 0 OID 19710)
-- Dependencies: 184
-- Data for Name: evento; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (1, false, 'Otros/Varios', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (3, false, 'Convención', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (4, false, 'Stock', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (6, false, 'Fiesta Fin de Año', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (7, false, 'Jornadas de Capacitación', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (8, false, 'A Campo', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (9, false, 'Talleres', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (10, false, 'Congreso', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (12, false, 'Stand', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (13, false, 'Almuerzo', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (16, false, 'Viajes', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (17, false, 'Adicionales', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (19, false, 'Diseños', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (20, false, 'Merchandising', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (21, false, 'Comisión', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (22, false, 'Long Drive', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (23, false, 'Jornada de Pesca', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (24, false, 'DTM', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (25, false, 'Coca Cola', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (26, false, 'Oficina', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (27, false, 'Kick Off brokers', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (28, false, 'Top Producers', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (29, false, 'Jornada Teodelina', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (30, false, 'AP LASE', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (14, true, 'Cena', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (5, true, 'Dashboard', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (11, true, 'Golf', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (2, true, 'Kids Tour', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (15, true, 'Noche de los Museos', false);
INSERT INTO evento (id, borrado, nombre, mostrar_a_solicitantes) VALUES (18, true, 'Presentación', false);


--
-- TOC entry 2334 (class 0 OID 0)
-- Dependencies: 185
-- Name: evento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('evento_id_seq', 30, true);


--
-- TOC entry 2256 (class 0 OID 19715)
-- Dependencies: 186
-- Data for Name: factura_compra; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (304, false, '2016-04-08 00:00:00', '2016-04-01 00:00:00', 178, 'S/F-00253/16', 18595.04, 3904.96, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (14, false, '2016-01-04 00:00:00', '2016-01-01 00:00:00', 11, '0002-00000010', 16000, 3360, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (15, false, '2016-01-04 00:00:00', '2016-01-01 00:00:00', 12, '0013-00012707', 5413.22, 1136.78, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (16, false, '2016-01-04 00:00:00', '2016-01-01 00:00:00', 12, '0013-00012708', 4725.39, 847.61, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (17, false, '2016-01-07 00:00:00', '2016-01-01 00:00:00', 12, '0013-00012736', 6398.98, 1198.02, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (27, false, '2016-01-08 00:00:00', '2016-01-01 00:00:00', 21, '0001-00000024', 4000, 840, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (8, false, '2016-01-06 00:00:00', '2016-01-01 00:00:00', 5, '0003-00001151', 12900, 2709, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (19, false, '2016-01-09 00:00:00', '2016-01-01 00:00:00', 14, '0002-00002185', 2796.5, 587.27, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (20, false, '2015-11-25 00:00:00', '2016-01-01 00:00:00', 14, '0001-00000666', 5735, 1204.35, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (1, false, '2016-01-06 00:00:00', '2016-01-01 00:00:00', 1, '0002-00000135', 22300, 4683, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (2, false, '2016-01-01 00:00:00', '2016-01-01 00:00:00', 2, '0002-00000307', 22956.5, 0, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (3, false, '2016-01-08 00:00:00', '2016-01-01 00:00:00', 4, 'S/F-00003/16', 102.7, 27.3, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (4, false, '2016-01-08 00:00:00', '2016-01-01 00:00:00', 4, 'S/F-00004/16', 3792, 1008, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (5, false, '2016-01-08 00:00:00', '2016-01-01 00:00:00', 4, 'S/F-00005/16', 790, 210, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (6, false, '2016-01-08 00:00:00', '2016-01-01 00:00:00', 4, 'S/F-00006/16', 237, 63, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (7, false, '2016-01-08 00:00:00', '2016-01-01 00:00:00', 4, 'S/F-00007/16', 790, 210, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (9, true, '2016-01-06 00:00:00', '2016-01-01 00:00:00', 6, '0041-00002355', 14013, 0, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (11, false, '2016-01-04 00:00:00', '2016-01-01 00:00:00', 7, '0033-00011394', 2712.31, 569.59, 0, 135.62, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (12, false, '2016-01-08 00:00:00', '2016-01-01 00:00:00', 9, 'S/F-00010/16', 7492.39, 1991.65, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (13, false, '2016-01-04 00:00:00', '2016-01-01 00:00:00', 10, '0002-00000118', 4485, 941.85, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (10, false, '2016-01-06 00:00:00', '2016-01-01 00:00:00', 6, '0041-00002355', 14013.39, 2942.81, 0, 700.67, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (18, false, '2016-01-06 00:00:00', '2016-01-01 00:00:00', 13, '0011-00028091', 24448.75, 50.41, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (22, false, '2015-11-30 00:00:00', '2016-01-01 00:00:00', 16, '0004-00043993', 3190, 669.9, 0, 159.5, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (23, false, '2016-01-12 00:00:00', '2016-01-01 00:00:00', 17, 'S/F-00016/16', 17380, 4620, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (21, false, '2015-11-05 00:00:00', '2016-01-01 00:00:00', 15, '0050-00000175', 3034, 637.14, 0, 151.7, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (24, false, '2015-12-17 00:00:00', '2016-01-01 00:00:00', 18, '0002-00001184', 239.67, 50.33, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (25, false, '2015-12-23 00:00:00', '2016-01-01 00:00:00', 19, '0006-00016481', 272.72, 57.27, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (26, false, '2015-12-15 00:00:00', '2016-01-01 00:00:00', 20, '0007-00000294', 687.62, 144.4, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (30, false, '2015-12-23 00:00:00', '2016-01-01 00:00:00', 23, '0001-00042250', 18000, 3780, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (34, false, '2016-01-18 00:00:00', '2016-01-01 00:00:00', 24, 'S/F-00025/16', 413.22, 86.78, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (35, false, '2016-01-18 00:00:00', '2016-01-01 00:00:00', 8, 'S/F-00026/16', 5289.26, 1110.74, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (32, false, '2015-11-03 00:00:00', '2016-01-01 00:00:00', 5, '0003-00001085', 840, 176.4, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (33, false, '2015-11-03 00:00:00', '2016-01-01 00:00:00', 5, '0003-00001086', 4680, 982.8, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (31, false, '2015-12-13 00:00:00', '2016-01-01 00:00:00', 5, '0003-00001125', 6222, 1306.62, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (36, false, '2016-01-05 00:00:00', '2016-01-01 00:00:00', 25, '0001-00000040', 6000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (37, false, '2016-01-20 00:00:00', '2016-01-01 00:00:00', 42, '6691-00007094', 589.83, 123.86, 0, 29.49, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (38, false, '2016-01-05 00:00:00', '2016-01-01 00:00:00', 43, '0117-00000389', 1231.4, 258.59, 0, 61.57, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (40, false, '2016-01-22 00:00:00', '2016-01-01 00:00:00', 65, 'S/F-00031/16', 2289.26, 480.74, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (41, false, '2016-01-20 00:00:00', '2016-01-01 00:00:00', 66, '0002-00000365', 3941.5, 827.72, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (42, false, '2016-01-22 00:00:00', '2016-01-01 00:00:00', 67, 'S/F-00033/16', 16528.93, 3471.07, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (43, false, '2016-01-22 00:00:00', '2016-01-01 00:00:00', 31, 'S/F-00034/16', 2394.47, 502.84, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (44, false, '2015-09-23 00:00:00', '2016-01-01 00:00:00', 68, '0206-00002310', 14840, 3116.4, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (29, false, '2016-01-14 00:00:00', '2016-01-01 00:00:00', 22, '0002-00000074', 1848, 388.08, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (45, false, '2016-01-20 00:00:00', '2016-01-01 00:00:00', 69, '0178-00001090', 30112.12, 6230.36, 0, 1928.44, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (46, false, '2016-01-19 00:00:00', '2016-01-01 00:00:00', 70, '0001-00000588', 1785, 375, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (47, false, '2016-01-15 00:00:00', '2016-01-01 00:00:00', 71, '0005-00000032', 1710.75, 359.26, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (48, false, '2015-12-18 00:00:00', '2016-01-01 00:00:00', 32, '0026-00002170', 2422.31, 508.69, 0, 121.11, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (49, false, '2016-01-22 00:00:00', '2016-01-01 00:00:00', 86, '0002-00000153', 1200, 252, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (51, false, '2016-01-25 00:00:00', '2016-01-01 00:00:00', 88, '0002-00000078', 800, 168, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (50, false, '2016-01-27 00:00:00', '2016-01-01 00:00:00', 87, '0002-00000204', 2040, 428.4, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (52, false, '2016-01-26 00:00:00', '2016-01-01 00:00:00', 26, '0005-00011215', 4404, 924.84, 220.2, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (53, false, '2016-01-27 00:00:00', '2016-01-01 00:00:00', 26, '0005-00011233', 4777, 1003.17, 238.85, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (57, false, '2016-02-01 00:00:00', '2016-02-01 00:00:00', 90, '0001-00000287', 30000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (61, false, '2015-12-15 00:00:00', '2016-01-01 00:00:00', 93, '0005-00003919', 103806, 21799.26, 0, 5190.3, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (58, false, '2016-01-29 00:00:00', '2016-01-01 00:00:00', 91, '0010-00001813', 44971.9, 9444.1, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (54, false, '2016-01-22 00:00:00', '2016-01-01 00:00:00', 59, '0005-00007230', 2998.35, 629.65, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (63, false, '2016-02-01 00:00:00', '2016-02-01 00:00:00', 10, '0002-00000142', 4485, 941.85, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (64, false, '2016-02-01 00:00:00', '2016-02-01 00:00:00', 2, '0002-00000353', 7982, 0, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (67, false, '2016-01-26 00:00:00', '2016-01-01 00:00:00', 95, '0001-00000065', 4340, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (55, false, '2016-01-20 00:00:00', '2016-01-01 00:00:00', 44, '0001-00000129', 39900, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (56, false, '2016-01-20 00:00:00', '2016-01-01 00:00:00', 44, '0001-00000131', 7410, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (66, false, '2016-01-15 00:00:00', '2016-01-01 00:00:00', 15, '0050-00000285', 2727.25, 572.72, 0, 136.36, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (65, false, '2016-01-31 00:00:00', '2016-01-01 00:00:00', 14, '0001-00002292', 1233, 258.93, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (59, false, '2016-01-26 00:00:00', '2016-01-01 00:00:00', 92, '0002-00000142', 6060, 1272.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (60, false, '2016-01-28 00:00:00', '2016-01-01 00:00:00', 92, '0002-00000143', 4400, 924, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (68, true, '2016-02-02 00:00:00', '2016-02-01 00:00:00', 94, '0001-00000032', 3250, 0, 0, 0, 1, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (62, false, '2016-02-02 00:00:00', '2016-02-01 00:00:00', 94, '0001-00000032', 3250, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (70, false, '2016-02-03 00:00:00', '2016-02-01 00:00:00', 96, '0013-00001080', 11040, 2318.4, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (69, false, '2016-02-04 00:00:00', '2016-02-01 00:00:00', 13, '0011-00028924', 14814.05, 1119.27, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (71, false, '2016-02-04 00:00:00', '2016-02-01 00:00:00', 60, '0001-00000021', 15000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (72, false, '2016-02-04 00:00:00', '2016-02-01 00:00:00', 97, '0002-00002512', 19550, 4105.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (74, false, '2015-12-07 00:00:00', '2016-01-01 00:00:00', 42, '5601-00014439', 1145.24, 222.55, 29.23, 57.28, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (75, false, '2016-01-29 00:00:00', '2016-01-01 00:00:00', 99, '1002-00025545', 803.61, 121.51, 0, 40.18, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (77, false, '2016-02-11 00:00:00', '2016-02-01 00:00:00', 100, '0001-00000502', 1500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (81, false, '2015-12-16 00:00:00', '2016-02-01 00:00:00', 102, '0028-00013535', 264.47, 55.54, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (83, true, '2016-02-12 00:00:00', '2016-02-01 00:00:00', 93, '0004-00005186', 51385.5, 10790.95, 0, 2569.28, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (84, false, '2016-02-12 00:00:00', '2016-02-01 00:00:00', 93, '0004-00005186', 51385.5, 10790.96, 0, 2569.28, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (87, true, '2016-02-12 00:00:00', '2016-02-01 00:00:00', 106, '0015-00003250', 2939.7, 0, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (86, false, '2016-02-11 00:00:00', '2016-02-01 00:00:00', 106, '0006-00004544', 6003.37, 1260.71, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (88, false, '2016-02-12 00:00:00', '2016-02-01 00:00:00', 106, '0015-00003250', 2939.7, 617.34, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (305, false, '2016-04-05 00:00:00', '2016-04-01 00:00:00', 152, '0001-00001926', 8562, 1798.02, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (92, true, '2015-09-24 00:00:00', '2016-02-01 00:00:00', 105, '0002-00000010', 52500, 11025, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (95, false, '2015-09-21 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000009', 73200, 15372, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (80, false, '2016-02-02 00:00:00', '2016-02-01 00:00:00', 101, '0010-00000009', 25500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (82, false, '2016-02-15 00:00:00', '2016-02-01 00:00:00', 103, '0003-00004608', 2066.12, 433.89, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (85, false, '2016-02-10 00:00:00', '2016-02-01 00:00:00', 104, '0002-00000267', 6320, 1327.2, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (73, false, '2016-02-03 00:00:00', '2016-02-01 00:00:00', 98, '0004-00026242', 3550.41, 745.59, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (76, false, '2016-02-08 00:00:00', '2016-02-01 00:00:00', 21, '0001-00000031', 4000, 840, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (39, false, '2016-01-21 00:00:00', '2016-01-01 00:00:00', 5, '0003-00001162', 420, 88.2, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (79, false, '2016-01-21 00:00:00', '2016-02-01 00:00:00', 5, '0003-00001164', 679, 142.59, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (78, false, '2016-02-15 00:00:00', '2016-02-01 00:00:00', 22, '0002-00000084', 3542.5, 743.92, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (100, false, '2015-06-22 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000002', 14200, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (101, false, '2015-06-29 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000003', 15500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (102, false, '2015-07-14 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000004', 15500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (108, false, '2015-08-21 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000010', 13000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (306, false, '2016-04-08 00:00:00', '2016-04-01 00:00:00', 5, '0003-00001223', 2650, 556.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (122, false, '2015-10-26 00:00:00', '2015-12-01 00:00:00', 110, '0003-00000001', 23500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (123, false, '2015-10-28 00:00:00', '2015-12-01 00:00:00', 110, '0003-00000002', 12450, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (124, false, '2015-10-30 00:00:00', '2015-12-01 00:00:00', 110, '0003-00000003', 20500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (119, false, '2015-06-04 00:00:00', '2015-12-01 00:00:00', 109, '0001-00000021', 10000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (120, false, '2015-07-01 00:00:00', '2015-12-01 00:00:00', 109, '0001-00000022', 10000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (121, false, '2015-08-03 00:00:00', '2015-12-01 00:00:00', 109, '0001-00000024', 15000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (116, false, '2015-09-11 00:00:00', '2015-12-01 00:00:00', 108, '0002-00000160', 27460, 5766.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (117, false, '2015-10-01 00:00:00', '2015-12-01 00:00:00', 108, '0002-00000169', 13862.31, 2911.09, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (118, false, '2015-11-12 00:00:00', '2015-12-01 00:00:00', 108, '0002-00000189', 10473.55, 2199.45, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (99, false, '2015-06-15 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000001', 12500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (103, false, '2015-07-21 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000005', 14100, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (104, false, '2015-07-27 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000006', 12600, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (105, false, '2015-07-31 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000007', 8500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (106, false, '2015-08-04 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000008', 12350, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (107, false, '2015-08-12 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000009', 14150, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (109, false, '2015-08-28 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000011', 14750, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (110, false, '2015-09-11 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000012', 11500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (111, false, '2015-09-17 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000013', 16350, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (112, false, '2015-09-25 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000014', 10150, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (113, false, '2015-10-26 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000015', 16500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (114, false, '2015-10-30 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000016', 15230, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (115, false, '2015-11-30 00:00:00', '2015-12-01 00:00:00', 67, '0002-00000017', 36650, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (93, false, '2015-09-24 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000010', 52500, 11025, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (96, false, '2015-10-31 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000028', 65979, 13855.59, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (97, false, '2015-10-31 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000029', 67800, 14238, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (91, false, '2015-10-31 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000030', 68200, 14322, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (94, false, '2015-10-31 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000027', 51323, 10777.83, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (89, false, '2015-11-03 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000035', 62723, 13171.83, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (90, false, '2015-11-30 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000034', 63627, 13361.67, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (98, false, '2015-11-30 00:00:00', '2015-12-01 00:00:00', 105, '0002-00000033', 75200, 15792, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (125, false, '2015-10-22 00:00:00', '2015-12-01 00:00:00', 111, '0004-00000323', 11726.7, 0, 0, 5773.3, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (126, false, '2016-01-26 00:00:00', '2016-02-01 00:00:00', 112, '0006-00000092', 401.65, 84.35, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (127, false, '2016-01-26 00:00:00', '2016-02-01 00:00:00', 113, '0002-00053072', 1497.52, 314.48, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (135, false, '2016-02-15 00:00:00', '2016-02-01 00:00:00', 116, '0007-00004006', 167286.5, 34965, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (134, true, '2016-02-22 00:00:00', '2016-02-01 00:00:00', 93, '0005-00003973', 12870, 2702.7, 0, 643.5, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (131, true, '2016-02-17 00:00:00', '2016-02-01 00:00:00', 115, '1213-00513874', 528, 0, 0, 0, 1, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (130, true, '2016-02-17 00:00:00', '2016-02-01 00:00:00', 114, '0001-00085298', 280, 0, 0, 0, 1, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (132, false, '2016-02-10 00:00:00', '2016-02-01 00:00:00', 10, '0002-00000150', 5534, 1162.14, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (136, false, '2016-02-22 00:00:00', '2016-02-01 00:00:00', 93, '0005-00083973', 12870, 2702.7, 0, 643.5, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (307, false, '2016-04-07 00:00:00', '2016-04-01 00:00:00', 193, '0002-00000483', 24300, 5103, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (310, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002508', 26860, 5640.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (149, false, '2016-02-24 00:00:00', '2016-02-01 00:00:00', 120, 'S/F-00092/16', 661.16, 138.84, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (150, false, '2016-02-24 00:00:00', '2016-02-01 00:00:00', 121, 'S/F-00093/16', 3900, 819, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (152, false, '2016-02-18 00:00:00', '2016-02-01 00:00:00', 122, '0002-00000618', 9728, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (153, false, '2016-02-18 00:00:00', '2016-02-01 00:00:00', 122, '0002-00000619', 850, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (154, false, '2016-01-17 00:00:00', '2016-02-01 00:00:00', 65, '4525-00005951', 1620.39, 305.66, 39.36, 76.77, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (155, false, '2016-02-01 00:00:00', '2016-02-01 00:00:00', 65, '4522-00017334', 514.87, 104.95, 0, 24.64, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (156, false, '2016-01-26 00:00:00', '2016-02-01 00:00:00', 123, '1457-00000955', 28598.7, 5612.3, 801.76, 1336.26, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (157, false, '2016-01-26 00:00:00', '2016-02-01 00:00:00', 123, '1457-00000953', 16857.01, 3310, 472.85, 788.09, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (158, false, '2016-02-19 00:00:00', '2016-02-01 00:00:00', 124, '0004-00001827', 37565.85, 7888.83, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (133, false, '2016-02-25 00:00:00', '2016-02-01 00:00:00', 94, '0001-00000035', 1479.34, 310.66, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (159, false, '2016-02-24 00:00:00', '2016-02-01 00:00:00', 93, '0005-00003980', 4055, 851.55, 0, 202.75, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (160, false, '2016-02-22 00:00:00', '2016-02-01 00:00:00', 91, '0010-00001843', 1144.63, 240.37, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (161, false, '2016-02-22 00:00:00', '2016-02-01 00:00:00', 91, '0010-00001842', 2152.89, 452.11, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (137, false, '2016-02-22 00:00:00', '2016-02-01 00:00:00', 117, '0002-00000007', 12000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (138, false, '2016-02-15 00:00:00', '2016-02-01 00:00:00', 97, '0002-00002516', 446.28, 93.72, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (139, false, '2016-02-17 00:00:00', '2016-02-01 00:00:00', 116, '0007-00004016', 10800, 2268, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (140, false, '2016-02-17 00:00:00', '2016-02-01 00:00:00', 116, '0007-00004017', 10000, 2100, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (141, false, '2016-02-15 00:00:00', '2016-02-01 00:00:00', 116, '0007-00004007', 20100, 4200, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (142, false, '2016-02-17 00:00:00', '2016-02-01 00:00:00', 118, '0002-00000038', 13158, 2763.18, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (162, false, '2016-02-23 00:00:00', '2016-02-01 00:00:00', 42, '5999-00009408', 961.95, 190.02, 25.43, 43.97, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (143, false, '2016-02-22 00:00:00', '2016-02-01 00:00:00', 87, '0002-00000211', 2070, 434.7, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (163, false, '2016-02-25 00:00:00', '2016-02-01 00:00:00', 116, '0007-00004035', 9600, 2016, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (165, false, '2016-02-25 00:00:00', '2016-02-01 00:00:00', 125, '0003-00000327', 17543.95, 3684.23, 0, 877.2, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (166, false, '2016-02-22 00:00:00', '2016-02-01 00:00:00', 127, '0002-00000987', 11000, 2310, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (319, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 195, '0001-00000001', 5625, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (144, false, '2016-02-06 00:00:00', '2016-02-01 00:00:00', 119, '0001-00002410', 49950, 10489.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (164, false, '2016-02-24 00:00:00', '2016-02-01 00:00:00', 26, '0005-00011326', 7165.5, 1504.76, 0, 358.28, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (128, false, '2016-02-17 00:00:00', '2016-02-01 00:00:00', 92, '0002-00000147', 2980, 625.8, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (129, false, '2016-02-17 00:00:00', '2016-02-01 00:00:00', 92, '0002-00000148', 4000, 840, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (151, false, '2016-02-18 00:00:00', '2016-02-01 00:00:00', 5, '0003-00001185', 10992, 2308.32, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (320, false, '2016-04-13 00:00:00', '2016-04-01 00:00:00', 195, '0001-00000002', 11825, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (167, false, '2016-02-24 00:00:00', '2016-02-01 00:00:00', 128, '0002-00000001', 1800, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (168, false, '2016-02-26 00:00:00', '2016-02-01 00:00:00', 13, 'S/F-00123/16', 12889.26, 2706.74, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (170, false, '2016-02-26 00:00:00', '2016-02-01 00:00:00', 13, '0010-00047012', 15005.4, 18.96, 571.56, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (145, false, '2016-02-10 00:00:00', '2016-02-01 00:00:00', 119, '0001-00002413', 50300, 10563, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (146, false, '2016-02-12 00:00:00', '2016-02-01 00:00:00', 119, '0001-00002417', 50190, 10539.9, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (148, false, '2016-02-15 00:00:00', '2016-02-01 00:00:00', 119, '0001-00002420', 49890, 10476.9, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (147, false, '2016-02-16 00:00:00', '2016-02-01 00:00:00', 119, '0001-00002423', 49670, 10430.7, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (181, false, '2016-03-02 00:00:00', '2016-03-01 00:00:00', 143, '0001-00000097', 3470, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (190, false, '2016-03-03 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000163', 2100, 441, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (171, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 2, '0002-00000397', 5635, 0, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (313, false, '2016-04-11 00:00:00', '2016-04-01 00:00:00', 194, '0002-00000183', 750, 157.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (312, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 21, '0001-00000043', 4000, 840, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (317, false, '2016-04-08 00:00:00', '2016-04-01 00:00:00', 2, '0002-00000455', 7315, 192.02, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (311, false, '2016-03-21 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002520', 24364, 5116.44, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (308, false, '2016-03-26 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002523', 25173, 5286.33, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (309, false, '2016-03-31 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002528', 23603, 4956.63, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (326, false, '2016-04-12 00:00:00', '2016-04-01 00:00:00', 128, '0002-00000003', 12000, 2520, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (327, false, '2016-04-11 00:00:00', '2016-04-01 00:00:00', 200, '0002-00000543', 8500, 1785, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (329, false, '2016-04-12 00:00:00', '2016-04-01 00:00:00', 199, '0002-00000003', 12000, 2520, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (333, false, '2016-03-17 00:00:00', '2016-04-01 00:00:00', 5, '0003-00001215', 3820, 802.2, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (334, false, '2016-04-21 00:00:00', '2016-04-01 00:00:00', 173, 'S/F-00288/16', 16528.93, 3471.07, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (325, false, '2016-03-08 00:00:00', '2016-04-01 00:00:00', 198, '0003-00042661', 5909.4, 1240.97, 0, 206.83, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (169, false, '2016-02-25 00:00:00', '2016-02-01 00:00:00', 86, '0002-00000173', 1106, 232.26, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (172, false, '2016-02-24 00:00:00', '2016-02-01 00:00:00', 141, '0450-00016930', 1995.87, 419.13, 59.88, 39.92, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (174, false, '2016-02-05 00:00:00', '2016-02-01 00:00:00', 142, '0011-00670775', 344.28, 72.3, 0, 17.21, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (175, false, '2016-02-11 00:00:00', '2016-02-01 00:00:00', 142, '0012-00695681', 775.97, 162.95, 0, 38.8, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (176, false, '2016-02-11 00:00:00', '2016-02-01 00:00:00', 142, '0012-00695682', 344.28, 72.3, 0, 17.21, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (177, false, '2016-02-05 00:00:00', '2016-02-01 00:00:00', 142, '0011-00670774', 775.97, 162.95, 0, 38.8, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (178, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 126, '0002-00000023', 45765, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (179, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 141, '0450-00017345', 103.31, 21.7, 0, 2.07, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (180, false, '2016-03-02 00:00:00', '2016-03-01 00:00:00', 141, 'S/F-00137/16', 1187.63, 249.4, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (182, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 144, '0001-00000003', 263000, 55230, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (184, false, '2016-03-02 00:00:00', '2016-03-01 00:00:00', 145, '0003-00005755', 285, 59.85, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (185, false, '2016-03-04 00:00:00', '2016-03-01 00:00:00', 74, '0013-00000131', 12369, 2597.49, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (187, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000157', 1750, 367.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (188, false, '2016-03-03 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000161', 4200, 882, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (189, false, '2016-03-03 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000162', 17860, 3750.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (173, false, '2016-02-24 00:00:00', '2016-02-01 00:00:00', 5, '0003-00001190', 3450, 724.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (191, false, '2016-03-07 00:00:00', '2016-03-01 00:00:00', 118, '0002-00000043', 13250, 2782.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (194, false, '2016-03-08 00:00:00', '2016-03-01 00:00:00', 94, '0001-00000037', 8745, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (195, false, '2016-03-07 00:00:00', '2016-03-01 00:00:00', 146, '0003-00000235', 491000, 103110, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (196, false, '2016-03-07 00:00:00', '2016-03-01 00:00:00', 146, '0003-00000236', 365170, 76685.7, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (197, false, '2016-03-05 00:00:00', '2016-03-01 00:00:00', 147, '0001-00000016', 7000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (198, false, '2016-03-07 00:00:00', '2016-03-01 00:00:00', 148, '0001-00000017', 180000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (186, false, '2016-03-04 00:00:00', '2016-03-01 00:00:00', 101, '0010-00000011', 23400, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (199, false, '2016-03-09 00:00:00', '2016-03-01 00:00:00', 149, '0002-00000163', 8673, 1821.33, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (200, false, '2016-03-09 00:00:00', '2016-03-01 00:00:00', 150, '0005-00000294', 1129.5, 237.2, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (193, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 21, '0001-00000037', 4000, 840, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (201, false, '2016-03-08 00:00:00', '2016-03-01 00:00:00', 151, '0001-00003201', 43760.4, 9189.68, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (202, false, '2016-03-08 00:00:00', '2016-03-01 00:00:00', 152, '0001-00001780', 9656.8, 2027.93, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (212, false, '2016-03-10 00:00:00', '2016-03-01 00:00:00', 127, '0002-00001020', 9600, 2016, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (208, false, '2016-03-10 00:00:00', '2016-03-01 00:00:00', 56, '0001-00000002', 13650, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (210, false, '2016-03-02 00:00:00', '2016-03-01 00:00:00', 157, '0003-00001784', 8345.46, 1752.55, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (211, false, '2016-03-10 00:00:00', '2016-03-01 00:00:00', 26, '0005-00011385', 41240, 8660.4, 0, 2062, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (213, false, '2016-03-10 00:00:00', '2016-03-01 00:00:00', 158, '0015-00000028', 45000, 9450, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (218, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002457', 33478, 7030.38, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (219, false, '2016-03-03 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002460', 40256, 8453.76, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (217, false, '2016-03-05 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002462', 29860, 6270.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (215, false, '2016-03-08 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002466', 20806, 4369.26, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (225, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 163, '0001-00000052', 14000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (226, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 164, '0006-00000159', 5643, 1185.03, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (221, false, '2016-03-11 00:00:00', '2016-03-01 00:00:00', 160, '0010-00002212', 1009.09, 211.91, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (207, false, '2016-03-10 00:00:00', '2016-03-01 00:00:00', 22, '0002-00000094', 6675, 1401.76, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (222, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 161, '0002-00000158', 4495.84, 944.13, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (223, false, '2016-03-11 00:00:00', '2016-03-01 00:00:00', 150, '0005-00000295', 1129.5, 237.2, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (224, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 162, '0003-00000037', 24500, 5145, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (227, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 165, '0002-00000147', 3678.4, 772.46, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (228, false, '2016-03-04 00:00:00', '2016-03-01 00:00:00', 130, '0001-00000053', 40000, 8400, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (203, false, '2016-03-08 00:00:00', '2016-03-01 00:00:00', 153, '0061-00001147', 4004, 840.84, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (230, false, '2016-03-03 00:00:00', '2016-03-01 00:00:00', 153, '0061-00001136', 6475, 1359.75, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (214, false, '2016-03-10 00:00:00', '2016-03-01 00:00:00', 144, '0001-00000005', 65200, 13692, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (231, false, '2016-03-15 00:00:00', '2016-03-01 00:00:00', 168, '0001-00000038', 38000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (233, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 147, '0001-00000017', 7000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (234, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 60, '0001-00000034', 15000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (235, false, '2016-03-15 00:00:00', '2016-03-01 00:00:00', 169, '0003-00000107', 240000, 50400, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (236, false, '2016-03-04 00:00:00', '2016-03-01 00:00:00', 166, '0001-00000056', 49750, 10447.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (237, false, '2016-03-04 00:00:00', '2016-03-01 00:00:00', 166, '0001-00000057', 49750, 10447.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (238, false, '2016-03-15 00:00:00', '2016-03-01 00:00:00', 170, '0001-00000010', 5200, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (229, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 167, '0021-00000414', 72560, 11457.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (239, false, '2016-03-15 00:00:00', '2016-03-01 00:00:00', 1, '0002-00000165', 8422, 1768.62, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (240, false, '2016-03-16 00:00:00', '2016-03-01 00:00:00', 171, '0003-00005217', 5152, 1081.92, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (241, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 121, '0002-00000016', 27360, 5745.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (209, false, '2016-02-29 00:00:00', '2016-02-01 00:00:00', 14, '0002-00002677', 2600, 546, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (216, false, '2016-03-07 00:00:00', '2016-03-01 00:00:00', 119, '0001-00002464', 25600, 5376, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (243, false, '2016-03-17 00:00:00', '2016-03-01 00:00:00', 9, 'S/F-00199/16', 5164.4, 1084.53, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (232, false, '2016-03-17 00:00:00', '2016-03-01 00:00:00', 94, '0001-00000038', 16275, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (258, false, '2016-03-03 00:00:00', '2016-03-01 00:00:00', 5, '0003-00001199', 1842.4, 386.9, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (246, false, '2016-03-16 00:00:00', '2016-03-01 00:00:00', 13, '0011-00029956', 6877.06, 492.03, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (206, false, '2016-03-09 00:00:00', '2016-03-01 00:00:00', 156, '0001-00000026', 520, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (205, false, '2016-03-09 00:00:00', '2016-03-01 00:00:00', 155, '0002-00000015', 24500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (204, false, '2016-03-09 00:00:00', '2016-03-01 00:00:00', 154, '0001-00000012', 25000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (316, false, '2016-04-11 00:00:00', '2016-04-01 00:00:00', 164, '0006-00000186', 6573.48, 1380.43, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (245, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 10, '0002-00000160', 4485, 941.85, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (244, false, '2016-03-16 00:00:00', '2016-03-01 00:00:00', 1, '0002-00000171', 12600, 2646, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (253, false, '2016-01-29 00:00:00', '2016-03-01 00:00:00', 175, '0039-00001953', 3884.3, 815.7, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (220, false, '2016-03-11 00:00:00', '2016-03-01 00:00:00', 159, '0001-00000096', 59250, 12442.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (268, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 181, '0002-00000068', 19200, 4032, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (265, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 74, '0012-00005411', 77704.2, 16317.88, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (259, false, '2016-03-03 00:00:00', '2016-03-01 00:00:00', 5, '0003-00001200', 1450, 304.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (250, false, '2016-03-18 00:00:00', '2016-04-01 00:00:00', 173, '0001-00000070', 3200, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (314, false, '2016-04-08 00:00:00', '2016-04-01 00:00:00', 153, '0061-00001278', 1193, 250.53, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (315, false, '2016-04-08 00:00:00', '2016-04-01 00:00:00', 153, '0061-00001277', 1925, 404.25, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (318, false, '2016-04-14 00:00:00', '2016-04-01 00:00:00', 121, 'S/F-00268/16', 2892.56, 607.44, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (322, false, '2016-04-04 00:00:00', '2016-04-01 00:00:00', 192, '0003-00000009', 47805, 10039.05, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (323, false, '2016-04-13 00:00:00', '2016-04-01 00:00:00', 50, '0001-00000116', 21600, 4536, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (328, false, '2016-04-13 00:00:00', '2016-04-01 00:00:00', 201, '0003-00000022', 40000, 8400, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (324, false, '2016-04-18 00:00:00', '2016-04-01 00:00:00', 162, '0003-00000042', 42000, 8820, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (330, false, '2016-04-21 00:00:00', '2016-04-01 00:00:00', 94, 'S/F-00284/16', 10760.33, 2259.67, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (331, false, '2016-04-21 00:00:00', '2016-04-01 00:00:00', 5, 'S/F-00285/16', 10560, 2217.6, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (332, false, '2016-03-17 00:00:00', '2016-04-01 00:00:00', 5, '0003-00001213', 616.8, 129.53, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (321, false, '2016-04-08 00:00:00', '2016-04-01 00:00:00', 196, '0001-00000056', 24000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (254, false, '2016-03-19 00:00:00', '2016-03-01 00:00:00', 176, '0002-00000056', 6840, 1436.4, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (252, false, '2016-03-04 00:00:00', '2016-03-01 00:00:00', 174, '0001-00000015', 31400, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (247, false, '2016-03-17 00:00:00', '2016-03-01 00:00:00', 13, '0011-00030019', 3641.46, 246.43, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (335, false, '2016-04-21 00:00:00', '2016-04-01 00:00:00', 186, 'S/F-00293/16', 12396.69, 2603.31, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (336, true, '2016-04-15 00:00:00', '2016-04-01 00:00:00', 202, '0002-00000252', 28000, 5880, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (338, false, '2016-04-13 00:00:00', '2016-04-01 00:00:00', 203, '0002-00000026', 21500, 4515, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (342, false, '2016-04-21 00:00:00', '2016-04-01 00:00:00', 182, 'S/F-00301/16', 983.47, 206.53, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (346, false, '2016-03-23 00:00:00', '2016-04-01 00:00:00', 92, '0002-00000169', 4235, 889.35, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (349, false, '2016-04-20 00:00:00', '2016-04-01 00:00:00', 206, '0002-00000240', 51000, 10710, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (352, false, '2016-04-18 00:00:00', '2016-04-01 00:00:00', 13, '0011-00031868', 13272.63, 27.37, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (248, false, '2016-03-17 00:00:00', '2016-03-01 00:00:00', 172, '0336-00000756', 31674.21, 3325.79, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (249, false, '2016-03-18 00:00:00', '2016-03-01 00:00:00', 94, 'S/F-00204/16', 13578.51, 2851.49, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (257, false, '2016-03-17 00:00:00', '2016-03-01 00:00:00', 94, '0001-00000039', 30225, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (260, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 160, '0010-00002266', 404.41, 84.93, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (251, false, '2016-03-17 00:00:00', '2016-03-01 00:00:00', 146, '0003-00000262', 29982, 6296.22, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (337, false, '2016-04-15 00:00:00', '2016-04-01 00:00:00', 202, '0002-00000252', 2800, 588, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (339, false, '2016-04-21 00:00:00', '2016-04-01 00:00:00', 3, 'S/F-00298/16', 21404.96, 4495.04, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (345, false, '2016-03-18 00:00:00', '2016-04-01 00:00:00', 92, '0002-00000168', 28020, 5884.2, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (255, false, '2016-03-21 00:00:00', '2016-03-01 00:00:00', 177, '0001-00000033', 10000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (256, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 178, 'S/F-00212/16', 18595.04, 3904.96, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (263, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 161, '0002-00000164', 1611.66, 338.44, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (340, false, '2016-04-20 00:00:00', '2016-04-01 00:00:00', 204, '0002-00000218', 80000, 16800, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (341, false, '2016-03-28 00:00:00', '2016-04-01 00:00:00', 152, '0001-00001862', 6678.3, 1402.44, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (343, false, '2016-04-12 00:00:00', '2016-04-01 00:00:00', 205, '0001-00000099', 7500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (344, false, '2016-04-07 00:00:00', '2016-04-01 00:00:00', 157, '0003-00002081', 2652.89, 557.11, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (347, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 92, '0002-00000179', 1020, 214.2, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (348, false, '2016-04-13 00:00:00', '2016-04-01 00:00:00', 92, '0003-00000007', 12764, 2680.44, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (350, false, '2016-04-20 00:00:00', '2016-04-01 00:00:00', 207, '0007-00000075', 33786.12, 7095.09, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (351, true, '2016-04-18 00:00:00', '2016-04-01 00:00:00', 13, '0011-00031868', 3272.63, 27.37, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (353, false, '2016-04-21 00:00:00', '2016-04-01 00:00:00', 163, '0001-00000055', 1600, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (262, false, '2016-03-17 00:00:00', '2016-03-01 00:00:00', 167, '0021-00000415', 59217.85, 11314.35, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (269, false, '2016-03-16 00:00:00', '2016-03-01 00:00:00', 182, '0001-00000489', 75000, 15750, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (264, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 179, '0001-00027863', 17000, 3570, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (266, false, '2016-03-21 00:00:00', '2016-03-01 00:00:00', 153, '0061-00001192', 4500, 945, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (270, false, '2016-03-23 00:00:00', '2016-03-01 00:00:00', 127, '0002-00001049', 9850, 2068.5, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (267, false, '2016-03-21 00:00:00', '2016-03-01 00:00:00', 180, '0003-00000026', 22710, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (271, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 181, '0002-00000067', 7600, 1596, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (274, false, '2016-03-23 00:00:00', '2016-03-01 00:00:00', 183, '0002-00000116', 624, 131.04, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (275, false, '2016-03-23 00:00:00', '2016-03-01 00:00:00', 184, '0002-00005077', 1512.4, 317.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (276, false, '2016-03-23 00:00:00', '2016-03-01 00:00:00', 185, '0003-00000068', 40000, 8400, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (278, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 187, '0002-00000008', 13000, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (277, true, '2016-03-29 00:00:00', '2016-03-01 00:00:00', 186, 'S/F-00230/16', 945500, 198555, 0, 0, 1, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (279, false, '2016-03-29 00:00:00', '2016-03-01 00:00:00', 186, '0006-00016700', 926000, 194460, 0, 0, 3, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (192, false, '2016-03-07 00:00:00', '2016-03-01 00:00:00', 5, '0003-00001202', 12060, 2532.6, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (242, false, '2016-03-14 00:00:00', '2016-03-01 00:00:00', 5, '0003-00001207', 406, 85.26, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (272, false, '2016-03-23 00:00:00', '2016-03-01 00:00:00', 5, '0003-00001217', 16884, 3545.64, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (273, false, '2016-03-23 00:00:00', '2016-03-01 00:00:00', 5, '0003-00001216', 256, 53.76, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (281, false, '2015-11-23 00:00:00', '2016-03-01 00:00:00', 188, '0005-00000208', 175, 36.75, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (282, false, '2016-03-29 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000172', 2400, 504, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (283, false, '2016-03-29 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000174', 608, 127.68, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (284, false, '2016-03-30 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000176', 500, 105, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (285, false, '2016-03-30 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000177', 4235, 889.35, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (287, false, '2016-03-30 00:00:00', '2016-03-01 00:00:00', 189, '0378-00039700', 10538.1, 2213.01, 316.15, 526.91, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (290, false, '2016-03-23 00:00:00', '2016-03-01 00:00:00', 10, '0002-00000175', 8564, 1798.44, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (291, false, '2016-03-31 00:00:00', '2016-04-01 00:00:00', 162, '0003-00000041', 15000, 3150, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (288, false, '2016-03-29 00:00:00', '2016-03-01 00:00:00', 185, '0003-00000069', 9400, 1974, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (289, false, '2016-03-28 00:00:00', '2016-03-01 00:00:00', 190, '0002-00000890', 4000, 840, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (298, false, '2016-04-06 00:00:00', '2016-04-01 00:00:00', 191, '0001-00039427', 1645, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (299, false, '2016-04-06 00:00:00', '2016-04-01 00:00:00', 191, '0001-00039426', 6580, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (293, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 149, '0002-00000178', 7434, 1561.14, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (286, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 94, '0001-00000043', 9860, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (295, false, '2016-04-05 00:00:00', '2016-04-01 00:00:00', 101, '0010-00000013', 23400, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (294, false, '2016-03-15 00:00:00', '2016-03-01 00:00:00', 174, '0001-00000018', 12500, 0, 0, 0, 2, 3);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (296, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 10, '0002-00000177', 4282, 899.22, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (297, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 10, '0002-00000178', 4485, 941.85, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (292, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 2, '0002-00000450', 10504, 0, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (301, false, '2016-04-01 00:00:00', '2016-04-01 00:00:00', 130, '0001-00000054', 51755, 10868.55, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (303, false, '2016-04-05 00:00:00', '2016-04-01 00:00:00', 118, '0002-00000049', 15100, 3171, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (302, false, '2016-04-06 00:00:00', '2016-04-01 00:00:00', 167, '0021-00000426', 13153.55, 1667.1, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (280, false, '2016-03-29 00:00:00', '2016-03-01 00:00:00', 5, '0003-00001219', 11590, 2433.9, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (300, false, '2016-03-04 00:00:00', '2016-03-01 00:00:00', 92, '0002-00000165', 11100, 2358, 0, 0, 2, 1);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (28, false, '2016-01-07 00:00:00', '2016-01-01 00:00:00', 12, '0013-00001068', 6398.9, 1198.1, 0, 0, 2, 7);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (183, false, '2016-03-01 00:00:00', '2016-03-01 00:00:00', 144, '0001-00000001', 3000, 630, 0, 0, 2, 7);
INSERT INTO factura_compra (id, borrado, fecha, mes_impositivo, proveedor_id, nro, subtotal, iva, perc_iva, perc_iibb, estado_factura_compra_id, tipo_factura_id) VALUES (261, false, '2016-03-22 00:00:00', '2016-03-01 00:00:00', 127, '0002-00000167', 9600, 2016, 0, 0, 2, 7);


--
-- TOC entry 2335 (class 0 OID 0)
-- Dependencies: 187
-- Name: factura_compra_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('factura_compra_id_seq', 353, true);


--
-- TOC entry 2258 (class 0 OID 19723)
-- Dependencies: 188
-- Data for Name: factura_compra_orden_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (76, 114, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (162, 115, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (1, 1, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (2, 2, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (3, 3, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (4, 4, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (5, 5, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (6, 6, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (7, 7, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (11, 9, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (12, 10, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (13, 11, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (10, 8, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (10, 12, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (18, 13, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (22, 15, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (23, 16, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (21, 17, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (24, 18, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (25, 19, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (26, 20, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (14, 21, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (15, 22, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (16, 22, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (17, 22, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (28, 22, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (27, 23, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (30, 24, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (34, 25, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (35, 26, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (32, 27, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (33, 27, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (31, 27, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (8, 27, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (36, 28, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (37, 29, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (38, 30, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (40, 31, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (41, 32, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (42, 33, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (43, 34, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (44, 35, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (29, 36, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (45, 37, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (46, 38, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (47, 39, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (48, 40, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (49, 43, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (51, 45, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (50, 46, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (52, 47, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (53, 47, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (61, 48, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (61, 49, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (58, 50, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (62, 53, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (54, 55, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (63, 56, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (64, 57, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (67, 58, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (57, 59, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (55, 60, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (56, 60, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (66, 61, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (19, 62, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (65, 62, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (59, 63, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (60, 63, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (70, 64, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (69, 65, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (71, 66, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (72, 68, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (74, 69, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (75, 70, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (77, 71, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (81, 72, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (84, 73, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (86, 74, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (88, 74, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (122, 75, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (123, 75, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (124, 75, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (119, 76, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (120, 76, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (121, 76, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (116, 77, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (117, 77, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (118, 77, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (99, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (100, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (101, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (102, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (103, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (104, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (105, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (106, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (107, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (108, 78, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (109, 79, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (110, 79, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (111, 79, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (112, 79, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (113, 79, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (114, 79, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (115, 79, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (95, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (93, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (96, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (97, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (91, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (94, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (89, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (90, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (98, 80, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (125, 81, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (126, 82, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (127, 83, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (131, 86, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (130, 85, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (132, 87, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (133, 88, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (80, 89, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (135, 90, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (136, 91, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (149, 92, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (150, 93, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (152, 94, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (153, 94, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (154, 95, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (155, 95, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (156, 97, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (157, 97, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (158, 98, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (159, 99, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (160, 100, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (161, 100, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (82, 103, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (85, 104, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (73, 105, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (137, 106, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (138, 107, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (139, 108, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (140, 109, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (141, 110, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (142, 111, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (142, 112, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (143, 117, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (163, 118, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (165, 119, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (166, 120, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (167, 122, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (168, 123, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (169, 124, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (170, 125, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (144, 126, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (145, 126, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (146, 126, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (148, 126, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (147, 126, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (172, 127, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (178, 134, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (164, 135, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (179, 136, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (180, 137, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (181, 138, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (182, 139, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (183, 139, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (184, 140, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (185, 142, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (128, 143, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (129, 143, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (187, 143, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (190, 143, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (188, 143, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (189, 143, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (20, 144, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (171, 145, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (39, 146, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (79, 146, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (151, 146, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (173, 146, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (191, 147, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (194, 148, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (195, 149, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (196, 149, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (78, 150, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (197, 151, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (198, 153, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (186, 155, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (199, 156, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (200, 157, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (193, 158, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (201, 159, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (202, 160, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (212, 161, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (208, 166, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (210, 167, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (211, 168, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (213, 169, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (218, 170, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (219, 170, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (217, 170, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (215, 170, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (221, 171, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (221, 173, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (220, 172, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (220, 174, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (222, 175, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (223, 176, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (224, 177, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (225, 178, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (226, 179, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (227, 180, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (228, 182, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (203, 183, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (230, 184, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (214, 185, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (231, 186, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (233, 188, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (234, 189, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (235, 190, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (236, 191, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (237, 191, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (238, 192, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (229, 193, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (229, 194, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (239, 195, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (240, 196, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (241, 197, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (209, 198, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (243, 199, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (245, 200, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (248, 202, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (249, 204, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (244, 205, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (250, 206, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (253, 207, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (254, 209, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (255, 210, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (252, 211, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (256, 212, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (232, 187, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (232, 213, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (257, 213, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (260, 214, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (262, 215, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (220, 216, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (269, 217, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (263, 218, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (264, 219, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (266, 220, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (261, 221, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (270, 221, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (267, 222, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (268, 223, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (271, 224, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (265, 225, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (274, 226, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (275, 227, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (276, 229, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (278, 231, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (277, 230, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (279, 232, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (207, 233, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (259, 234, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (258, 234, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (192, 234, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (242, 234, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (272, 234, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (273, 234, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (246, 235, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (247, 235, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (281, 236, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (206, 237, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (205, 238, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (204, 239, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (282, 240, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (283, 240, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (284, 240, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (285, 240, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (286, 241, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (287, 242, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (290, 243, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (291, 246, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (288, 247, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (289, 248, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (298, 249, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (299, 249, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (293, 250, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (295, 251, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (294, 252, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (304, 253, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (296, 254, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (297, 254, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (292, 255, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (305, 256, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (313, 259, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (301, 260, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (312, 261, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (314, 262, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (315, 262, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (303, 263, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (302, 264, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (316, 265, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (317, 266, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (318, 268, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (319, 269, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (320, 270, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (307, 257, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (307, 272, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (216, 258, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (310, 258, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (311, 258, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (308, 258, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (309, 258, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (216, 273, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (310, 273, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (311, 273, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (308, 273, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (309, 273, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (216, 274, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (310, 274, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (311, 274, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (308, 274, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (309, 274, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (251, 277, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (322, 278, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (323, 279, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (327, 280, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (328, 281, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (324, 282, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (329, 283, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (330, 284, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (331, 285, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (333, 286, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (332, 286, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (280, 286, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (306, 286, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (334, 288, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (325, 289, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (321, 290, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (335, 293, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (337, 295, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (338, 296, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (339, 298, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (340, 299, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (341, 300, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (342, 301, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (343, 302, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (300, 287, true);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (300, 303, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (345, 303, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (346, 303, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (347, 303, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (348, 303, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (349, 304, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (350, 305, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (352, 306, false);
INSERT INTO factura_compra_orden_pago (factura_compra_id, orden_pago_id, borrado) VALUES (353, 307, false);


--
-- TOC entry 2280 (class 0 OID 20024)
-- Dependencies: 210
-- Data for Name: factura_solicitud_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2336 (class 0 OID 0)
-- Dependencies: 211
-- Name: factura_solicitud_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('factura_solicitud_pago_id_seq', 1, false);


--
-- TOC entry 2259 (class 0 OID 19726)
-- Dependencies: 189
-- Data for Name: modo_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO modo_pago (id, borrado, nombre, descripcion) VALUES (1, false, 'Sin Factura', 'Para casos especiales donde no se trabaja con factura.');
INSERT INTO modo_pago (id, borrado, nombre, descripcion) VALUES (2, false, 'Efectivo', NULL);
INSERT INTO modo_pago (id, borrado, nombre, descripcion) VALUES (3, false, 'Cheque', NULL);
INSERT INTO modo_pago (id, borrado, nombre, descripcion) VALUES (4, false, 'Transferencia', NULL);
INSERT INTO modo_pago (id, borrado, nombre, descripcion) VALUES (5, false, 'Transferencia a tercero', NULL);
INSERT INTO modo_pago (id, borrado, nombre, descripcion) VALUES (6, false, 'Transferencia sin proveedor', 'Usada en los casos de OP SinFact y SinProv.');
INSERT INTO modo_pago (id, borrado, nombre, descripcion) VALUES (7, false, 'Tarjeta Credito', NULL);


--
-- TOC entry 2337 (class 0 OID 0)
-- Dependencies: 190
-- Name: modo_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('modo_pago_id_seq', 7, true);


--
-- TOC entry 2261 (class 0 OID 19734)
-- Dependencies: 191
-- Data for Name: orden_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (11, false, '2016-01-08 00:00:00', '00011/16', 1, 'Mantenimiento y configuración', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (18, false, '2016-01-13 00:00:00', '00018/16', 1, 'Auriculares Angel', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (20, false, '2016-01-13 00:00:00', '00020/16', 1, 'Farmacia', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (27, false, '2016-01-19 00:00:00', '00027/16', 1, 'Saldo', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (29, false, '2016-01-21 00:00:00', '00029/16', 1, 'Compra coto oficina', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (30, false, '2016-01-21 00:00:00', '00030/16', 1, 'Ticket vendedor torres katherine', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (33, false, '2016-01-22 00:00:00', '00033/16', 1, 'Comision laura Finning', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (35, false, '2016-01-22 00:00:00', '00035/16', 1, 'Por ingreso de Wanda Reyes', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (40, false, '2016-01-22 00:00:00', '00040/16', 13, 'Hotel San Nicolas gastos', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (53, false, '2016-02-02 00:00:00', '00053/16', 1, 'Fletes', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (56, false, '2016-02-03 00:00:00', '00056/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (57, false, '2016-02-03 00:00:00', '00057/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (62, false, '2016-02-03 00:00:00', '00062/16', 1, 'Servicio de mensajeria', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (63, false, '2016-02-03 00:00:00', '00063/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (69, false, '2016-02-11 00:00:00', '00069/16', 26, 'Compras oficina', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (70, false, '2016-02-11 00:00:00', '00070/16', 1, 'Nafta', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (72, false, '2016-02-17 00:00:00', '00072/16', 1, 'Aceite', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (86, true, '2016-02-23 00:00:00', '00086/16', 1, 'Easy', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (85, true, '2016-02-23 00:00:00', '00085/16', 13, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (87, false, '2016-02-23 00:00:00', '00087/16', 26, 'Toner xerox', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (88, false, '2016-02-23 00:00:00', '00088/16', 1, 'Fletes', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (89, false, '2016-02-24 00:00:00', '00089/16', 19, 'Trabajos de diseño', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (93, false, '2016-02-24 00:00:00', '00093/16', 20, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (94, false, '2016-02-24 00:00:00', '00094/16', 1, 'Comida conserva', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (100, false, '2016-02-25 00:00:00', '00100/16', 23, 'Pago saldo cena 1 y 2', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (138, false, '2016-03-02 00:00:00', '00138/16', 1, 'Moto', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (143, false, '2016-03-07 00:00:00', '00143/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (144, false, '2016-03-07 00:00:00', '00144/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (145, false, '2016-03-07 00:00:00', '00145/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (146, false, '2016-03-07 00:00:00', '00146/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (148, false, '2016-03-08 00:00:00', '00148/16', 1, 'Fletes', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (150, false, '2016-03-08 00:00:00', '00150/16', 20, 'gorros', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (198, false, '2016-03-16 00:00:00', '00198/16', 1, 'Motos', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (207, false, '2016-03-22 00:00:00', '00207/16', 1, 'Particular', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (200, false, '2016-03-17 00:00:00', '00200/16', 26, 'Mantenimiento pc', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (204, false, '2016-03-18 00:00:00', '00204/16', 1, 'Flete', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (234, false, '2016-03-31 00:00:00', '00234/16', 1, 'Tritone saldo', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (236, false, '2016-03-31 00:00:00', '00236/16', 1, 'vinilo', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (237, false, '2016-03-31 00:00:00', '00237/16', 26, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (239, false, '2016-03-31 00:00:00', '00239/16', 1, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (240, false, '2016-03-31 00:00:00', '00240/16', 1, 'lonas', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (241, false, '2016-03-31 00:00:00', '00241/16', 1, 'Fletes', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (243, false, '2016-03-31 00:00:00', '00243/16', 26, NULL, 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (251, false, '2016-04-08 00:00:00', '00251/16', 1, 'Trabajos de diseño', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (254, false, '2016-04-08 00:00:00', '00254/16', 26, 'Mantenimiento PC', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (255, false, '2016-04-08 00:00:00', '00255/16', 1, 'Remis', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (266, false, '2016-04-14 00:00:00', '00266/16', 1, 'Remis', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (269, false, '2016-04-14 00:00:00', '00269/16', 26, 'transporte', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (270, false, '2016-04-14 00:00:00', '00270/16', 1, 'Transporte', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (284, false, '2016-04-21 00:00:00', '00284/16', 1, 'Fletes', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (286, false, '2016-04-21 00:00:00', '00286/16', 1, 'Impresiones', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (292, false, '2016-04-21 00:00:00', '00292/16', 1, 'Pagos varios', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (303, false, '2016-04-22 00:00:00', '00303/16', 1, 'Impresiones varias', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (60, false, '2016-02-03 00:00:00', '00060/16', 1, 'Aladino catering', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (61, false, '2016-02-03 00:00:00', '00061/16', 1, 'Marketing', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (1, false, '2016-01-08 00:00:00', '00001/16', 1, NULL, 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (2, false, '2016-01-08 00:00:00', '00002/16', 1, 'Remises', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (3, false, '2016-01-08 00:00:00', '00003/16', 1, 'Remax Day Use propina', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (4, false, '2016-01-08 00:00:00', '00004/16', 1, 'Remax Retiro brokers San Nicolás - Staff', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (5, false, '2016-01-08 00:00:00', '00005/16', 1, 'Fiesta de Fin de año Buhl - Staff', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (6, false, '2016-01-08 00:00:00', '00006/16', 1, 'Celular', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (7, false, '2016-01-08 00:00:00', '00007/16', 1, 'Remax Day Use', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (9, false, '2016-01-08 00:00:00', '00009/16', 1, 'Remax Day Use 18/12', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (10, false, '2016-01-08 00:00:00', '00010/16', 16, 'Viajes nacionales', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (8, true, '2016-01-08 00:00:00', '00008/16', 1, 'Trebol 4 Day Use', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (12, false, '2016-01-11 00:00:00', '00012/16', 1, 'Trebol4 Day Use', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (13, false, '2016-01-11 00:00:00', '00013/16', 16, '2 Pasajes Sakima/Carmona', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (14, false, '2016-01-12 00:00:00', '00014/16', 1, NULL, 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (15, false, '2016-01-12 00:00:00', '00015/16', 22, 'Limpieza', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (16, false, '2016-01-12 00:00:00', '00016/16', 8, 'Campo Pem-Washington, catering', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (17, false, '2016-01-12 00:00:00', '00017/16', 22, 'Pelotas de golf', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (19, false, '2016-01-13 00:00:00', '00019/16', 1, 'Bidones de agua', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (21, false, '2016-01-14 00:00:00', '00021/16', 1, 'Coca Cola fotografía', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (22, false, '2016-01-15 00:00:00', '00022/16', 16, 'Viajes nacionales - 4 paquetes', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (23, false, '2016-01-15 00:00:00', '00023/16', 1, 'Cartel publicitario T4- Mes Enero', 3, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (24, false, '2016-01-18 00:00:00', '00024/16', 20, 'Manuales volvo', 12, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (25, false, '2016-01-18 00:00:00', '00025/16', 1, 'Celular Fc enero', 12, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (26, false, '2016-01-18 00:00:00', '00026/16', 8, 'Fotografía - Jornada campo PEM-Washington', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (28, false, '2016-01-19 00:00:00', '00028/16', 22, 'Pagina del Long Drive revista', 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (31, false, '2016-01-22 00:00:00', '00031/16', 1, 'Gift card Escandinavia del plata', 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (32, false, '2016-01-22 00:00:00', '00032/16', 1, 'Llaveros colgantes', 12, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (34, false, '2016-01-22 00:00:00', '00034/16', 8, 'Gira herbicidas y jornada PEM - traslados', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (36, false, '2016-01-22 00:00:00', '00036/16', 20, 'Biogenesis Bagó', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (37, false, '2016-01-22 00:00:00', '00037/16', 1, 'Remax retiro de brokers', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (38, false, '2016-01-22 00:00:00', '00038/16', 1, 'Mobiliario Aimar - basf', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (39, false, '2016-01-22 00:00:00', '00039/16', 1, 'Mobiliario aimar- basf', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (43, false, '2016-01-26 00:00:00', '00043/16', 1, 'Porta credenciales', 12, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (44, false, '2016-01-28 00:00:00', '00044/16', 1, 'Publicidad revista', 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (45, false, '2016-01-28 00:00:00', '00045/16', 1, NULL, 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (46, false, '2016-01-28 00:00:00', '00046/16', 1, NULL, 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (47, false, '2016-01-28 00:00:00', '00047/16', 4, NULL, 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (48, false, '2016-02-02 00:00:00', '00048/16', 1, NULL, 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (49, false, '2016-02-02 00:00:00', '00049/16', 1, NULL, 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (50, false, '2016-02-02 00:00:00', '00050/16', 1, 'Pesca', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (51, false, '2016-02-02 00:00:00', '00051/16', 1, 'Coca Cola', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (52, false, '2016-02-02 00:00:00', '00052/16', 21, NULL, 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (54, false, '2016-02-03 00:00:00', '00054/16', 1, 'Basf', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (55, false, '2016-02-03 00:00:00', '00055/16', 7, NULL, 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (58, false, '2016-02-03 00:00:00', '00058/16', 1, 'Mobiliario', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (59, false, '2016-02-03 00:00:00', '00059/16', 10, NULL, 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (64, false, '2016-02-04 00:00:00', '00064/16', 25, 'Sombreros', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (66, false, '2016-02-04 00:00:00', '00066/16', 25, 'Humor', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (67, false, '2016-02-05 00:00:00', '00067/16', 1, 'Bago - Pato', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (68, false, '2016-02-05 00:00:00', '00068/16', 1, 'Reunion Staff Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (71, false, '2016-02-17 00:00:00', '00071/16', 26, 'Reparacion mochila baño', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (73, false, '2016-02-17 00:00:00', '00073/16', 24, 'Hotel', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (74, false, '2016-02-22 00:00:00', '00074/16', 1, 'Angel alojamiento', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (82, false, '2016-02-22 00:00:00', '00082/16', 13, NULL, 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (83, false, '2016-02-22 00:00:00', '00083/16', 16, 'Alojamiento', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (90, false, '2016-02-24 00:00:00', '00090/16', 1, 'Kick Off brokers 18/02 y desayuno top prod 19/02', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (91, false, '2016-02-24 00:00:00', '00091/16', 24, 'Hotel', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (92, false, '2016-02-24 00:00:00', '00092/16', 3, 'Cena restaurante', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (96, false, '2016-02-24 00:00:00', '00096/16', 1, NULL, 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (98, false, '2016-02-24 00:00:00', '00098/16', 20, 'Libretas ecológicas RYDSA', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (99, false, '2016-02-25 00:00:00', '00099/16', 24, 'Hotel', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (103, false, '2016-02-25 00:00:00', '00103/16', 8, 'Salón quimilí', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (104, false, '2016-02-25 00:00:00', '00104/16', 29, 'Servicios prestados', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (105, false, '2016-02-25 00:00:00', '00105/16', 3, 'Chocolates personalizados', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (106, false, '2016-02-25 00:00:00', '00106/16', 8, 'Catering quimilí', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (107, false, '2016-02-25 00:00:00', '00107/16', 17, 'Reunion staff 12-02 villa maria adicionales', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (108, false, '2016-02-25 00:00:00', '00108/16', 27, 'Escenario evento 18 y 19 feb', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (109, false, '2016-02-25 00:00:00', '00109/16', 27, 'Livings evento 18-12', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (110, false, '2016-02-25 00:00:00', '00110/16', 27, 'Salón 2do evento brokers 26-04 y 7-07', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (111, false, '2016-02-25 00:00:00', '00111/16', 28, 'Acreditación + credenciales', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (112, false, '2016-02-25 00:00:00', '00112/16', 28, 'Acreditación + credencales', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (113, false, '2016-02-25 00:00:00', '00113/16', 1, 'Encomienda corriente - envio sillas y mesas- Daniel zorrilla', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (114, false, '2016-02-25 00:00:00', '00114/16', 1, 'Cartel publicitario T4 mes febrero', 3, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (117, false, '2016-02-26 00:00:00', '00117/16', 1, 'Placa reconocimiento Finning', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (118, false, '2016-02-26 00:00:00', '00118/16', 27, 'Pantallas y proyección eventos 18/02 y 19/02', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (119, false, '2016-02-26 00:00:00', '00119/16', 1, 'Trebol 4 gacebos', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (120, false, '2016-02-26 00:00:00', '00120/16', 3, 'Bolsas ecológicas', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (121, false, '2016-02-26 00:00:00', '00121/16', 25, 'Flores', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (122, false, '2016-02-26 00:00:00', '00122/16', 25, 'Cajones de madera', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (123, false, '2016-02-26 00:00:00', '00123/16', 3, 'Pasajes aereos', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (124, false, '2016-02-29 00:00:00', '00124/16', 25, 'Credenciales', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (125, false, '2016-03-01 00:00:00', '00125/16', 3, 'Viajes convencion', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (126, false, '2016-03-01 00:00:00', '00126/16', 3, 'Decoradora', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (129, false, '2016-03-02 00:00:00', '00129/16', 3, 'Kick off centros de mesa- convencion inspección-regalo Play', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (130, false, '2016-03-02 00:00:00', '00130/16', 27, 'Centros de mesa', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (131, false, '2016-03-02 00:00:00', '00131/16', 8, 'Coordinación evento', 4, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (132, false, '2016-03-02 00:00:00', '00132/16', 26, 'Stock cajas deposito', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (133, false, '2016-03-02 00:00:00', '00133/16', 1, 'Personal', 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (134, false, '2016-03-02 00:00:00', '00134/16', 25, 'Mobiliario', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (135, false, '2016-03-02 00:00:00', '00135/16', 20, 'Gorras Finning', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (136, false, '2016-03-02 00:00:00', '00136/16', 26, 'Repuesto maquina nespresso', 3, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (137, false, '2016-03-02 00:00:00', '00137/16', 26, 'Servicio técnico maquinas nespresso', 3, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (140, false, '2016-03-03 00:00:00', '00140/16', 26, 'Equipo aromatización', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (141, false, '2016-03-04 00:00:00', '00141/16', 25, 'Gastos durante evento', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (142, false, '2016-03-07 00:00:00', '00142/16', 7, 'Rosario 10/3', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (147, false, '2016-03-07 00:00:00', '00147/16', 3, 'Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (149, false, '2016-03-08 00:00:00', '00149/16', 25, 'CATERING', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (151, false, '2016-03-08 00:00:00', '00151/16', 25, 'Actuación', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (152, false, '2016-03-08 00:00:00', '00152/16', 1, 'Basf', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (153, false, '2016-03-08 00:00:00', '00153/16', 3, 'Remax', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (154, false, '2016-03-09 00:00:00', '00154/16', 21, 'Redes y Distribuciòn', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (156, false, '2016-03-10 00:00:00', '00156/16', 3, 'Cintas colgantes saldo', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (157, false, '2016-03-10 00:00:00', '00157/16', 30, 'Cintas colgantes', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (158, false, '2016-03-10 00:00:00', '00158/16', 1, 'Cartel publicitario trebol4', 3, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (159, false, '2016-03-10 00:00:00', '00159/16', 30, 'Powerbanks', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (160, false, '2016-03-10 00:00:00', '00160/16', 30, 'Regalos', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (41, false, '2016-01-26 00:00:00', '00041/16', 6, NULL, 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (161, false, '2016-03-11 00:00:00', '00161/16', 3, 'bolsas ecológicas', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (162, false, '2016-03-11 00:00:00', '00162/16', 25, 'Ayudante evento', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (163, false, '2016-03-11 00:00:00', '00163/16', 30, 'Equipo basf', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (164, false, '2016-03-11 00:00:00', '00164/16', 3, 'Gastos convención', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (166, false, '2016-03-11 00:00:00', '00166/16', 1, 'Premios Acrílico 3M', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (167, false, '2016-03-11 00:00:00', '00167/16', 12, 'Bolígrafos expoagro', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (168, false, '2016-03-11 00:00:00', '00168/16', 12, 'Finning expoagro Gorras', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (169, false, '2016-03-11 00:00:00', '00169/16', 25, 'Salón evento', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (170, false, '2016-03-11 00:00:00', '00170/16', 3, 'Pago decoración 25%', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (171, true, '2016-03-14 00:00:00', '00171/16', 3, 'Sellos', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (173, false, '2016-03-14 00:00:00', '00173/16', 3, 'Sellos', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (172, true, '2016-03-14 00:00:00', '00172/16', 30, 'Almuerzo en parador pago 50%', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (174, false, '2016-03-14 00:00:00', '00174/16', 30, 'Almuerzo en paradero pago 50%', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (175, false, '2016-03-15 00:00:00', '00175/16', 30, 'Premios', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (176, false, '2016-03-15 00:00:00', '00176/16', 30, 'Cintas colgantes', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (177, false, '2016-03-15 00:00:00', '00177/16', 30, 'Adelanto ambientación', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (178, false, '2016-03-15 00:00:00', '00178/16', 30, 'DJ', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (179, false, '2016-03-15 00:00:00', '00179/16', 30, 'Tele bastidor', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (180, false, '2016-03-15 00:00:00', '00180/16', 3, 'Lavar fundas', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (181, false, '2016-03-15 00:00:00', '00181/16', 1, 'Paul gastos', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (182, false, '2016-03-15 00:00:00', '00182/16', 3, 'Adelanto extras MARIANO ANDRES HONIG', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (183, false, '2016-03-15 00:00:00', '00183/16', 25, 'Alquiler handies', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (184, false, '2016-03-15 00:00:00', '00184/16', 3, 'Alquiler handies', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (186, false, '2016-03-16 00:00:00', '00186/16', 3, 'APLase', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (188, false, '2016-03-16 00:00:00', '00188/16', 25, 'Actuación', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (189, false, '2016-03-16 00:00:00', '00189/16', 25, 'mago', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (192, false, '2016-03-16 00:00:00', '00192/16', 30, NULL, 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (193, false, '2016-03-16 00:00:00', '00193/16', 3, 'Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (194, false, '2016-03-16 00:00:00', '00194/16', 3, 'Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (195, false, '2016-03-16 00:00:00', '00195/16', 30, 'grafica', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (165, true, '2016-03-11 00:00:00', '00165/16', 1, 'Logistica', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (196, false, '2016-03-16 00:00:00', '00196/16', 3, 'Remax', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (197, false, '2016-03-16 00:00:00', '00197/16', 3, 'Remax', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (199, false, '2016-03-17 00:00:00', '00199/16', 30, 'Pasajes', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (209, false, '2016-03-22 00:00:00', '00209/16', 12, 'Cancon 13 stand Mendoza- Servicios varios', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (211, false, '2016-03-22 00:00:00', '00211/16', 3, 'Orador', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (214, false, '2016-03-22 00:00:00', '00214/16', 3, 'Remax', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (221, false, '2016-03-23 00:00:00', '00221/16', 1, '3 M', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (201, false, '2016-03-18 00:00:00', '00201/16', 1, 'Eventos varios coordinación', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (202, false, '2016-03-18 00:00:00', '00202/16', 30, 'Bus traslado', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (205, false, '2016-03-18 00:00:00', '00205/16', 3, 'Credenciales', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (206, false, '2016-03-18 00:00:00', '00206/16', 1, 'Fotografías sitio web', 3, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (216, false, '2016-03-23 00:00:00', '00216/16', 30, 'Almuerzo en parador pago 50% saldo', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (203, false, '2016-03-18 00:00:00', '00203/16', 30, 'Viaticos', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (213, false, '2016-03-22 00:00:00', '00213/16', 3, 'Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (215, false, '2016-03-23 00:00:00', '00215/16', 3, 'Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (218, false, '2016-03-23 00:00:00', '00218/16', 30, NULL, 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (219, false, '2016-03-23 00:00:00', '00219/16', 3, 'Remax', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (217, false, '2016-03-23 00:00:00', '00217/16', 30, NULL, 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (220, false, '2016-03-23 00:00:00', '00220/16', 30, NULL, 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (208, false, '2016-03-22 00:00:00', '00208/16', 8, 'Evento San Jeronimo- personal externo', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (210, false, '2016-03-22 00:00:00', '00210/16', 30, 'Orador', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (212, false, '2016-03-22 00:00:00', '00212/16', 3, 'Show fun night adelanto 50%', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (187, true, '2016-03-16 00:00:00', '00187/16', 3, 'Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (222, false, '2016-03-23 00:00:00', '00222/16', 25, 'promotoras', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (223, false, '2016-03-23 00:00:00', '00223/16', 25, NULL, 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (224, false, '2016-03-23 00:00:00', '00224/16', 1, 'Remax', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (225, false, '2016-03-28 00:00:00', '00225/16', 9, 'Evento Rosario 27 y 28 Abril', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (226, false, '2016-03-28 00:00:00', '00226/16', 1, '3M Porta nombres', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (227, false, '2016-03-28 00:00:00', '00227/16', 26, 'Patas de pantalla', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (228, false, '2016-03-28 00:00:00', '00228/16', 30, 'Efectivo evento', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (229, false, '2016-03-28 00:00:00', '00229/16', 3, 'Orador Magui', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (230, true, '2016-03-29 00:00:00', '00230/16', 3, 'Hotel', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (232, false, '2016-03-31 00:00:00', '00232/16', 3, 'Hotel', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (233, false, '2016-03-31 00:00:00', '00233/16', 20, 'Bordado gorros', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (235, false, '2016-03-31 00:00:00', '00235/16', 16, 'Pasajes', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (238, false, '2016-03-31 00:00:00', '00238/16', 1, NULL, 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (242, false, '2016-03-31 00:00:00', '00242/16', 30, 'regalos', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (244, false, '2016-04-01 00:00:00', '00244/16', 30, 'Impresiones varias', 14, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (245, false, '2016-04-01 00:00:00', '00245/16', 30, 'Excursión Saldo', 4, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (246, false, '2016-04-01 00:00:00', '00246/16', 30, 'Ambientación', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (247, false, '2016-04-01 00:00:00', '00247/16', 3, 'Pasaje Magui', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (248, false, '2016-04-01 00:00:00', '00248/16', 30, 'Orador', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (250, false, '2016-04-07 00:00:00', '00250/16', 3, 'Remax', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (252, false, '2016-04-08 00:00:00', '00252/16', 3, 'orador', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (253, false, '2016-04-08 00:00:00', '00253/16', 3, 'Show fun night', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (256, false, '2016-04-08 00:00:00', '00256/16', 30, 'box cuisine', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (259, false, '2016-04-12 00:00:00', '00259/16', 1, NULL, 7, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (260, false, '2016-04-12 00:00:00', '00260/16', 3, 'Remax', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (261, false, '2016-04-12 00:00:00', '00261/16', 26, NULL, 3, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (262, false, '2016-04-14 00:00:00', '00262/16', 3, '5 handies adicionales + corbatero faltante', 16, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (263, false, '2016-04-14 00:00:00', '00263/16', 3, 'Acreditación', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (264, false, '2016-04-14 00:00:00', '00264/16', 3, 'Traslado', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (265, false, '2016-04-14 00:00:00', '00265/16', 1, 'Arcor 13 de abril - pago tarima', 4, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (257, true, '2016-04-11 00:00:00', '00257/16', 3, 'Flete fusión eventos', 6, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (272, false, '2016-04-15 00:00:00', '00272/16', 3, NULL, 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (258, true, '2016-04-11 00:00:00', '00258/16', 3, 'Decoración saldo 25%', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (273, true, '2016-04-15 00:00:00', '00273/16', 3, 'decoracion', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (274, false, '2016-04-15 00:00:00', '00274/16', 3, 'Decoracion', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (190, false, '2016-03-16 00:00:00', '00190/16', 3, 'Remax', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (275, false, '2016-04-15 00:00:00', '00275/16', 25, 'ambientacion - pato calvi', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (276, false, '2016-04-15 00:00:00', '00276/16', 1, 'particular', 15, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (277, false, '2016-04-15 00:00:00', '00277/16', 25, 'Catering', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (278, false, '2016-04-15 00:00:00', '00278/16', 25, NULL, 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (279, false, '2016-04-18 00:00:00', '00279/16', 7, 'Catering - Técnica de maní', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (280, false, '2016-04-21 00:00:00', '00280/16', 30, 'Diseño prezi', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (281, false, '2016-04-21 00:00:00', '00281/16', 19, 'Diseño pag web BASF adelanto', 4, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (282, false, '2016-04-21 00:00:00', '00282/16', 30, 'Ambientación', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (283, false, '2016-04-21 00:00:00', '00283/16', 3, 'atencion', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (285, false, '2016-04-21 00:00:00', '00285/16', 1, 'Trag saldo', 2, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (288, false, '2016-04-21 00:00:00', '00288/16', 25, 'Fotografo', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (289, false, '2016-04-21 00:00:00', '00289/16', 3, 'Boligrafos', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (290, false, '2016-04-21 00:00:00', '00290/16', 30, 'Diseño posters', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (291, false, '2016-04-21 00:00:00', '00291/16', 25, 'Coordinación - SOFIA', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (293, false, '2016-04-21 00:00:00', '00293/16', 3, 'Propina hotel', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (294, false, '2016-04-21 00:00:00', '00294/16', 3, 'SADIAC', 18, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (295, false, '2016-04-21 00:00:00', '00295/16', 1, 'Arcor- 13 abril', 4, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (296, false, '2016-04-21 00:00:00', '00296/16', 30, 'edicion videos', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (297, false, '2016-04-21 00:00:00', '00297/16', 30, 'Fotografía', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (298, false, '2016-04-21 00:00:00', '00298/16', 30, 'Video Jornadas', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (301, false, '2016-04-21 00:00:00', '00301/16', 30, 'Viaticos Orador taller', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (302, false, '2016-04-21 00:00:00', '00302/16', 30, 'Coordinacion evento', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (287, true, '2016-04-21 00:00:00', '00287/16', 1, 'Coca cola -  tachos, lonas, impresiones', 17, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (304, false, '2016-04-22 00:00:00', '00304/16', 20, 'Pendrive 4GB', 10, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (306, false, '2016-04-22 00:00:00', '00306/16', 30, 'Pasajes oradores', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (307, false, '2016-04-22 00:00:00', '00307/16', 30, 'Viaticos DJ', 11, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (42, false, '2016-01-26 00:00:00', '00042/16', 1, NULL, 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (65, false, '2016-02-04 00:00:00', '00065/16', 3, 'Remax', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (95, false, '2016-02-24 00:00:00', '00095/16', 1, 'Compras', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (97, false, '2016-02-24 00:00:00', '00097/16', 1, NULL, 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (115, false, '2016-02-25 00:00:00', '00115/16', 1, 'Compras', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (127, false, '2016-03-02 00:00:00', '00127/16', 26, 'Capsulas Nespresso', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (116, true, '2016-02-26 00:00:00', '00116/16', 26, 'Cápsulas nesspreso', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (155, false, '2016-03-09 00:00:00', '00155/16', 1, NULL, 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (191, false, '2016-03-16 00:00:00', '00191/16', 25, NULL, 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (249, false, '2016-04-07 00:00:00', '00249/16', 1, 'Finning', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (268, false, '2016-04-14 00:00:00', '00268/16', 25, NULL, 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (271, false, '2016-04-14 00:00:00', '00271/16', 26, 'Escritorio', 13, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (75, false, '2016-02-22 00:00:00', '00075/16', 1, NULL, 9, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (76, false, '2016-02-22 00:00:00', '00076/16', 1, NULL, 9, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (77, false, '2016-02-22 00:00:00', '00077/16', 1, NULL, 9, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (78, false, '2016-02-22 00:00:00', '00078/16', 1, NULL, 9, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (79, false, '2016-02-22 00:00:00', '00079/16', 1, NULL, 9, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (80, false, '2016-02-22 00:00:00', '00080/16', 1, NULL, 9, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (81, false, '2016-02-22 00:00:00', '00081/16', 16, NULL, 9, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (84, false, '2016-02-22 00:00:00', '00084/16', 12, 'Adelanto', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (101, false, '2016-02-25 00:00:00', '00101/16', 12, NULL, 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (102, false, '2016-02-25 00:00:00', '00102/16', 12, NULL, 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (128, false, '2016-03-02 00:00:00', '00128/16', 3, 'Kick Off centros de mesa-convencion inspection', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (139, false, '2016-03-02 00:00:00', '00139/16', 12, 'Expo-agro', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (185, false, '2016-03-15 00:00:00', '00185/16', 25, 'stand', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (231, false, '2016-03-29 00:00:00', '00231/16', 25, 'Grabacion y edición', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (267, false, '2016-04-14 00:00:00', '00267/16', 12, 'Expo agro - Finning Cat', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (299, false, '2016-04-21 00:00:00', '00299/16', 12, '3M stand', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (300, false, '2016-04-21 00:00:00', '00300/16', 12, '3M- Seguridad', 8, NULL);
INSERT INTO orden_pago (id, borrado, fecha, nro, evento_id, concepto, usuario_solicitante_id, solicitud_pago_id) VALUES (305, false, '2016-04-22 00:00:00', '00305/16', 12, 'Safety Division - 3M', 8, NULL);


--
-- TOC entry 2338 (class 0 OID 0)
-- Dependencies: 192
-- Name: orden_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('orden_pago_id_seq', 307, true);


--
-- TOC entry 2263 (class 0 OID 19742)
-- Dependencies: 193
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (70, false, 3, '3974', 3, '2016-02-05 00:00:00', NULL, NULL, 3176, 67, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (71, false, 3, '3975', 3, '2016-02-12 00:00:00', NULL, NULL, 23655.5, 68, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (1, false, 3, '3923', 3, '2016-01-21 00:00:00', NULL, NULL, 26983, 1, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (2, false, 3, '3925', 3, '2016-01-18 00:00:00', NULL, NULL, 22956.5, 2, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (3, false, 2, NULL, NULL, NULL, NULL, NULL, 130, 3, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (4, false, 2, NULL, NULL, NULL, NULL, NULL, 4800, 4, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (5, false, 2, NULL, NULL, NULL, NULL, NULL, 1000, 5, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (6, false, 2, NULL, NULL, NULL, NULL, NULL, 300, 6, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (7, false, 2, NULL, NULL, NULL, NULL, NULL, 1000, 7, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (9, false, 4, NULL, NULL, NULL, NULL, NULL, 3417.52, 9, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (10, false, 7, NULL, NULL, NULL, NULL, NULL, 9484.04, 10, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (11, false, 3, '3922', 3, '2016-01-06 00:00:00', NULL, NULL, 5426.85, 11, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (8, true, 4, NULL, NULL, NULL, NULL, NULL, 17656.87, 8, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (12, false, 3, '3933', 3, '2016-01-21 00:00:00', NULL, NULL, 17656.87, 12, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (13, false, 4, NULL, NULL, NULL, NULL, NULL, 24499.16, 13, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (14, false, 3, '7512', 1, '2016-01-12 00:00:00', NULL, NULL, 10000, 14, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (15, false, 3, '3934', 3, '2016-01-12 00:00:00', NULL, NULL, 20000, 14, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (16, false, 4, NULL, NULL, NULL, NULL, NULL, 4019.4, 15, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (17, false, 4, NULL, NULL, NULL, NULL, NULL, 22000, 16, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (18, false, 4, NULL, NULL, NULL, NULL, NULL, 3822.84, 17, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (19, false, 2, NULL, NULL, NULL, NULL, NULL, 290, 18, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (20, false, 2, NULL, NULL, NULL, NULL, NULL, 329.99, 19, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (21, false, 2, NULL, NULL, NULL, NULL, NULL, 832.02, 20, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (22, false, 3, '3936', 3, '2016-01-19 00:00:00', NULL, NULL, 19360, 21, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (23, false, 4, NULL, NULL, NULL, NULL, NULL, 12123, 22, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (24, false, 4, NULL, NULL, NULL, NULL, NULL, 4840, 23, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (25, false, 4, NULL, NULL, NULL, NULL, NULL, 21780, 24, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (26, false, 2, NULL, NULL, NULL, NULL, NULL, 500, 25, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (27, false, 3, '3931', 3, '2016-01-15 00:00:00', NULL, NULL, 6400, 26, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (28, false, 3, '3937', 3, '2016-02-19 00:00:00', NULL, NULL, 29816.82, 27, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (29, false, 3, '3938', 3, '2016-02-06 00:00:00', NULL, NULL, 6000, 28, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (30, false, 7, NULL, NULL, NULL, NULL, NULL, 743.18, 29, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (31, false, 2, NULL, NULL, NULL, NULL, NULL, 1551.56, 30, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (32, false, 2, NULL, NULL, NULL, NULL, NULL, 2770, 31, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (33, false, 3, '3947', 3, '2016-02-04 00:00:00', NULL, NULL, 4769.22, 32, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (34, false, 3, '3945', 3, '2016-02-04 00:00:00', NULL, NULL, 20000, 33, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (35, false, 4, NULL, NULL, NULL, NULL, NULL, 2897.31, 34, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (36, false, 4, NULL, NULL, NULL, NULL, NULL, 17956.4, 35, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (37, false, 3, '3948', 3, '2016-02-14 00:00:00', NULL, NULL, 2236.08, 36, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (38, false, 3, '3946', 3, '2016-02-23 00:00:00', NULL, NULL, 38270.92, 37, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (39, false, 2, NULL, NULL, NULL, NULL, NULL, 2160, 38, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (40, false, 2, NULL, NULL, NULL, NULL, NULL, 2070.01, 39, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (41, false, 4, NULL, NULL, NULL, NULL, NULL, 3052.11, 40, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (42, false, 3, '3950', 3, '2016-02-02 00:00:00', NULL, NULL, 34000, 41, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (43, false, 3, '3951', 3, '2016-02-02 00:00:00', NULL, NULL, 30000, 41, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (44, false, 3, '3952', 3, '2016-02-02 00:00:00', NULL, NULL, 15000, 42, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (45, false, 2, NULL, NULL, NULL, NULL, NULL, 1452, 43, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (46, false, 3, '3953', 3, '2016-02-06 00:00:00', NULL, NULL, 6000, 44, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (47, false, 3, '3954', 3, '2016-02-02 00:00:00', NULL, NULL, 968, 45, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (48, false, 3, '3955', 3, '2016-02-02 00:00:00', NULL, NULL, 2468.4, 46, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (49, false, 3, '3956', 3, '2016-01-26 00:00:00', NULL, NULL, 11568.06, 47, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (50, false, 7, NULL, NULL, NULL, NULL, NULL, 65397.78, 48, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (51, false, 7, NULL, NULL, NULL, NULL, NULL, 65397.78, 49, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (52, false, 7, NULL, NULL, NULL, NULL, NULL, 54416, 50, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (53, false, 3, '3961', 3, '2016-02-03 00:00:00', NULL, NULL, 30000, 51, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (54, false, 3, '3962', 3, '2016-02-03 00:00:00', NULL, NULL, 30040, 52, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (55, false, 3, '3963', 3, '2016-02-04 00:00:00', NULL, NULL, 50000, 52, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (56, false, 3, '3964', 3, '2016-02-04 00:00:00', NULL, NULL, 4830, 53, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (57, false, 3, '3965', 3, '2016-02-04 00:00:00', NULL, NULL, 5000, 54, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (58, false, 4, NULL, NULL, NULL, NULL, NULL, 3628, 55, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (59, false, 4, NULL, NULL, NULL, NULL, NULL, 5426.85, 56, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (60, false, 3, '3967', 3, '2016-02-20 00:00:00', NULL, NULL, 7982, 57, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (61, false, 2, NULL, NULL, NULL, NULL, NULL, 4340, 58, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (62, false, 4, NULL, NULL, NULL, NULL, NULL, 30000, 59, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (63, false, 4, NULL, NULL, NULL, NULL, NULL, 47310, 60, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (64, false, 4, NULL, NULL, NULL, NULL, NULL, 3436.33, 61, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (65, false, 3, '3968', 3, '2016-02-17 00:00:00', NULL, NULL, 4875.7, 62, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (66, false, 3, '3969', 3, '2016-02-28 00:00:00', NULL, NULL, 12656.6, 63, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (67, false, 3, '3970', 3, '2016-02-29 00:00:00', NULL, NULL, 13358.4, 64, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (68, false, 4, NULL, NULL, NULL, NULL, NULL, 15933.32, 65, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (69, false, 3, '3971', 3, '2016-03-03 00:00:00', NULL, NULL, 15000, 66, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (72, false, 7, NULL, NULL, NULL, NULL, NULL, 1454.3, 69, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (73, false, 7, NULL, NULL, NULL, NULL, NULL, 965.3, 70, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (74, false, 2, NULL, NULL, NULL, NULL, NULL, 1500, 71, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (75, false, 2, NULL, NULL, NULL, NULL, NULL, 320.01, 72, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (76, false, 7, NULL, NULL, NULL, NULL, NULL, 64745.74, 73, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (77, false, 2, NULL, NULL, NULL, NULL, NULL, 10821.12, 74, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (78, false, 2, NULL, NULL, NULL, NULL, NULL, 56450, 75, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (79, false, 2, NULL, NULL, NULL, NULL, NULL, 35000, 76, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (80, false, 2, NULL, NULL, NULL, NULL, NULL, 62673, 77, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (81, false, 2, NULL, NULL, NULL, NULL, NULL, 132400, 78, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (82, false, 2, NULL, NULL, NULL, NULL, NULL, 121130, 79, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (83, false, 2, NULL, NULL, NULL, NULL, NULL, 702467.92, 80, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (84, false, 2, NULL, NULL, NULL, NULL, NULL, 17500, 81, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (85, false, 2, NULL, NULL, NULL, NULL, NULL, 486, 82, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (86, false, 2, NULL, NULL, NULL, NULL, NULL, 1812, 83, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (87, false, 3, '3979', 3, '2016-02-23 00:00:00', NULL, NULL, 50000, 84, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (88, false, 3, '3978', 3, '2016-02-22 00:00:00', NULL, NULL, 50000, 84, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (90, true, 2, NULL, NULL, NULL, NULL, NULL, 528, 86, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (89, true, 2, NULL, NULL, NULL, NULL, NULL, 280, 85, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (91, false, 4, NULL, NULL, NULL, NULL, NULL, 6696.14, 87, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (92, false, 3, '3980', 3, '2016-02-25 00:00:00', NULL, NULL, 1790, 88, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (93, false, 2, NULL, NULL, NULL, NULL, NULL, 25500, 89, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (94, false, 3, '9388', 3, '2016-02-17 00:00:00', NULL, NULL, 80000, 90, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (95, false, 3, '9389', 3, '2016-02-26 00:00:00', NULL, NULL, 122251.5, 90, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (96, false, 7, NULL, NULL, NULL, NULL, NULL, 16216.2, 91, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (97, false, 7, NULL, NULL, NULL, NULL, NULL, 800, 92, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (98, false, 3, '9390', 3, '2016-02-29 00:00:00', NULL, NULL, 4719, 93, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (99, false, 7, NULL, NULL, NULL, NULL, NULL, 10578, 94, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (100, false, 2, NULL, NULL, NULL, NULL, NULL, 2686.64, 95, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (101, false, 3, '9391', 3, '2016-02-24 00:00:00', NULL, NULL, 5000, 96, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (102, false, 2, NULL, NULL, NULL, NULL, NULL, 57776.97, 97, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (103, false, 7, NULL, NULL, NULL, NULL, NULL, 45454.68, 98, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (104, false, 7, NULL, NULL, NULL, NULL, NULL, 5109.3, 99, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (105, false, 7, NULL, NULL, NULL, NULL, NULL, 3990, 100, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (106, false, 3, '0140', 1, '2016-02-29 00:00:00', NULL, NULL, 17400, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (107, false, 3, '4422', 8, '2016-02-26 00:00:00', NULL, NULL, 28500, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (108, false, 3, '9393', 3, '2016-03-06 00:00:00', NULL, NULL, 54100, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (109, false, 3, '9394', 3, '2016-03-25 00:00:00', NULL, NULL, 60000, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (110, false, 3, '9395', 3, '2016-04-07 00:00:00', NULL, NULL, 60000, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (111, false, 3, '9396', 3, '2016-04-22 00:00:00', NULL, NULL, 60000, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (112, false, 3, '9397', 3, '2016-05-05 00:00:00', NULL, NULL, 60000, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (115, false, 4, NULL, NULL, NULL, NULL, NULL, 2500.01, 103, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (113, false, 3, '9398', 3, '2016-05-20 00:00:00', NULL, NULL, 60000, 101, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (114, false, 3, '9399', 3, '2016-06-06 00:00:00', NULL, NULL, 60000, 102, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (118, false, 4, NULL, NULL, NULL, NULL, NULL, 12000, 106, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (119, false, 4, NULL, NULL, NULL, NULL, NULL, 540, 107, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (126, false, 4, NULL, NULL, NULL, NULL, NULL, 4840, 114, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (127, false, 2, NULL, NULL, NULL, NULL, NULL, 1221.37, 115, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (130, false, 4, NULL, NULL, NULL, NULL, NULL, 11616, 118, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (131, false, 4, NULL, NULL, NULL, NULL, NULL, 22105.38, 119, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (134, false, 2, NULL, NULL, NULL, NULL, NULL, 1800, 122, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (135, false, 4, NULL, NULL, NULL, NULL, NULL, 6382.84, 123, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (136, false, 7, NULL, NULL, NULL, NULL, NULL, 9213.16, 123, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (138, false, 4, NULL, NULL, NULL, NULL, NULL, 9213.08, 125, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (139, false, 7, NULL, NULL, NULL, NULL, NULL, 6382.84, 125, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (140, false, 3, '9409', 3, '2016-03-08 00:00:00', NULL, NULL, 60439.5, 126, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (141, false, 3, '9410', 3, '2016-03-08 00:00:00', NULL, NULL, 60863, 126, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (142, false, 3, '9411', 3, '2016-03-08 00:00:00', NULL, NULL, 60729.9, 126, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (143, false, 3, '9412', 3, '2016-03-08 00:00:00', NULL, NULL, 60366.9, 126, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (144, false, 3, '9413', 3, '2016-03-08 00:00:00', NULL, NULL, 60100.7, 126, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (147, false, 2, NULL, NULL, NULL, NULL, NULL, 5980, 129, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (149, false, 2, NULL, NULL, NULL, NULL, NULL, 4000, 131, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (156, false, 3, '9418', 3, '2016-03-03 00:00:00', NULL, NULL, 3470, 138, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (157, false, 3, '9393', 3, '2016-03-06 00:00:00', NULL, NULL, 54100, 139, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (158, false, 3, '9394', 3, '2016-03-25 00:00:00', NULL, NULL, 60000, 139, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (159, false, 3, '9403', 3, '2016-03-28 00:00:00', NULL, NULL, 20500, 139, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (160, false, 3, '9395', 3, '2016-04-07 00:00:00', NULL, NULL, 60000, 139, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (161, false, 3, '9396', 3, '2016-04-22 00:00:00', NULL, NULL, 60000, 139, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (162, false, 3, '9397', 3, '2016-05-05 00:00:00', NULL, NULL, 60000, 139, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (116, false, 4, NULL, NULL, NULL, NULL, NULL, 7647.2, 104, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (117, false, 4, NULL, NULL, NULL, NULL, NULL, 4296, 105, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (120, false, 4, NULL, NULL, NULL, NULL, NULL, 13068, 108, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (121, false, 4, NULL, NULL, NULL, NULL, NULL, 12100, 109, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (122, false, 4, NULL, NULL, NULL, NULL, NULL, 24300, 110, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (123, false, 4, NULL, NULL, NULL, NULL, NULL, 7960.59, 111, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (124, false, 4, NULL, NULL, NULL, NULL, NULL, 7960.59, 112, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (125, false, 6, NULL, NULL, NULL, NULL, 75, 1480, 113, '20-29262773-5');
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (129, false, 3, '9400', 3, '2016-02-26 00:00:00', NULL, NULL, 2504.7, 117, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (132, false, 4, NULL, NULL, NULL, NULL, NULL, 13310, 120, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (133, false, 3, '9401', 3, '2016-02-29 00:00:00', NULL, NULL, 22000, 121, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (137, false, 4, NULL, NULL, NULL, NULL, NULL, 1338.26, 124, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (145, false, 7, NULL, NULL, NULL, NULL, NULL, 2514.8, 127, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (128, true, 2, NULL, NULL, NULL, NULL, NULL, 2514.79, 116, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (146, false, 2, NULL, NULL, NULL, NULL, NULL, 2672, 128, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (148, false, 2, NULL, NULL, NULL, NULL, NULL, 500, 130, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (150, false, 2, NULL, NULL, NULL, NULL, NULL, 660, 132, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (151, false, 3, '9414', 3, '2016-03-02 00:00:00', NULL, NULL, 3520, 133, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (152, false, 3, '9415', 3, '2016-04-04 00:00:00', NULL, NULL, 45765, 134, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (153, false, 3, '9416', 3, '2016-04-04 00:00:00', NULL, NULL, 9028.54, 135, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (154, false, 7, NULL, NULL, NULL, NULL, NULL, 127.08, 136, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (155, false, 7, NULL, NULL, NULL, NULL, NULL, 1437.03, 137, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (163, false, 2, NULL, NULL, NULL, NULL, NULL, 344.85, 140, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (164, false, 3, '9419', 3, '2016-03-07 00:00:00', NULL, NULL, 5000, 141, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (165, false, 7, NULL, NULL, NULL, NULL, NULL, 14966.49, 142, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (166, false, 3, '9425', 3, '2016-04-08 00:00:00', NULL, NULL, 39796.9, 143, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (167, false, 3, '9426', 3, '2016-03-10 00:00:00', NULL, NULL, 6939.35, 144, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (168, false, 3, '9427', 3, '2016-03-15 00:00:00', NULL, NULL, 5635, 145, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (169, false, 3, '9428', 3, '2016-04-18 00:00:00', NULL, NULL, 18804.61, 146, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (170, false, 3, '9429', 3, '2016-03-21 00:00:00', NULL, NULL, 16032.5, 147, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (171, false, 3, '9430', 3, '2016-03-08 00:00:00', NULL, NULL, 8745, 148, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (172, false, 3, '9431', 3, '2016-03-22 00:00:00', NULL, NULL, 250000, 149, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (173, false, 3, '9432', 3, '2016-03-27 00:00:00', NULL, NULL, 250000, 149, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (174, false, 3, '9433', 3, '2016-04-08 00:00:00', NULL, NULL, 250000, 149, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (175, false, 3, '9434', 3, '2016-04-16 00:00:00', NULL, NULL, 285965.7, 149, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (176, false, 3, '9435', 3, '2016-03-28 00:00:00', NULL, NULL, 4286.42, 150, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (177, false, 3, '9436', 3, '2016-03-08 00:00:00', NULL, NULL, 7000, 151, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (178, false, 3, '9439', 3, '2016-03-08 00:00:00', NULL, NULL, 20000, 152, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (179, false, 3, '9420', 3, '2016-03-11 00:00:00', NULL, NULL, 50000, 153, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (180, false, 3, '9421', 3, '2016-03-18 00:00:00', NULL, NULL, 40000, 153, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (181, false, 3, '9422', 3, '2016-03-25 00:00:00', NULL, NULL, 45000, 153, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (182, false, 3, '9423', 3, '2016-04-08 00:00:00', NULL, NULL, 45000, 153, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (183, false, 3, '9442', 3, '2016-03-16 00:00:00', NULL, NULL, 20000, 154, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (184, false, 3, '5761', 2, '2016-03-03 00:00:00', NULL, NULL, 23400, 155, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (185, false, 4, NULL, NULL, NULL, NULL, NULL, 10494.33, 156, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (186, false, 4, NULL, NULL, NULL, NULL, NULL, 1366.7, 157, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (187, false, 4, NULL, NULL, NULL, NULL, NULL, 4840, 158, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (188, false, 7, NULL, NULL, NULL, NULL, NULL, 52950.08, 159, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (189, false, 7, NULL, NULL, NULL, NULL, NULL, 11684.73, 160, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (190, false, 4, NULL, NULL, NULL, NULL, NULL, 11616, 161, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (191, false, 3, '9453', 3, '2016-03-18 00:00:00', NULL, NULL, 7200, 162, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (192, false, 3, '9451', 3, '2016-03-16 00:00:00', NULL, NULL, 30000, 163, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (193, false, 3, '9452', 3, '2016-03-27 00:00:00', NULL, NULL, 40000, 164, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (195, false, 3, '9448', 3, '2016-03-22 00:00:00', NULL, NULL, 13650, 166, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (196, false, 3, '9447', 3, '2016-03-11 00:00:00', NULL, NULL, 10098.01, 167, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (197, false, 3, '9446', 3, '2016-03-21 00:00:00', NULL, NULL, 51962.4, 168, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (198, false, 3, '9445', 3, '2016-04-11 00:00:00', NULL, NULL, 54450, 169, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (199, false, 3, '9455', 3, '2016-03-18 00:00:00', NULL, NULL, 25175.26, 170, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (200, false, 3, '9456', 3, '2016-03-18 00:00:00', NULL, NULL, 36130.6, 170, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (201, false, 3, '9457', 3, '2016-03-18 00:00:00', NULL, NULL, 40508.38, 170, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (202, false, 3, '9458', 3, '2016-03-18 00:00:00', NULL, NULL, 48709.76, 170, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (203, true, 4, NULL, NULL, NULL, NULL, NULL, 610.5, 171, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (205, false, 4, NULL, NULL, NULL, NULL, NULL, 1221, 173, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (204, true, 4, NULL, NULL, NULL, NULL, NULL, 17923.13, 172, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (206, false, 4, NULL, NULL, NULL, NULL, NULL, 35846.25, 174, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (207, false, 4, NULL, NULL, NULL, NULL, NULL, 5439.97, 175, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (208, false, 4, NULL, NULL, NULL, NULL, NULL, 1366.7, 176, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (209, false, 4, NULL, NULL, NULL, NULL, NULL, 29645, 177, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (210, false, 4, NULL, NULL, NULL, NULL, NULL, 14000, 178, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (211, false, 4, NULL, NULL, NULL, NULL, NULL, 6828.03, 179, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (212, false, 3, '9460', 3, '2016-03-18 00:00:00', NULL, NULL, 4450.86, 180, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (213, false, 3, '9462', 3, '2016-03-18 00:00:00', NULL, NULL, 35070, 181, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (214, false, 3, '9461', 3, '2016-03-22 00:00:00', NULL, NULL, 48400, 182, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (215, false, 4, NULL, NULL, NULL, NULL, NULL, 4844.84, 183, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (216, false, 4, NULL, NULL, NULL, NULL, NULL, 7834.75, 184, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (217, false, 3, '9464', 3, '2016-03-25 00:00:00', NULL, NULL, 19500, 185, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (218, false, 3, '9465', 3, '2016-04-04 00:00:00', NULL, NULL, 19500, 185, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (219, false, 3, '9466', 3, '2016-04-11 00:00:00', NULL, NULL, 19500, 185, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (220, false, 3, '9467', 3, '2016-04-18 00:00:00', NULL, NULL, 20392, 185, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (221, false, 3, '9471', 3, '2016-03-18 00:00:00', NULL, NULL, 19000, 186, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (222, false, 3, '9472', 3, '2016-04-04 00:00:00', NULL, NULL, 19000, 186, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (225, false, 3, '9469', 3, '2016-03-15 00:00:00', NULL, NULL, 7000, 188, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (226, false, 3, '9470', 3, '2016-03-15 00:00:00', NULL, NULL, 15000, 189, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (227, false, 3, '9473', 3, '2016-03-21 00:00:00', NULL, NULL, 96800, 190, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (228, false, 3, '9474', 3, '2016-04-21 00:00:00', NULL, NULL, 96800, 190, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (229, false, 3, '9475', 3, '2016-05-21 00:00:00', NULL, NULL, 96800, 190, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (230, false, 3, '9444', 3, '2016-03-17 00:00:00', NULL, NULL, 49750, 191, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (231, false, 3, '9454', 3, '2016-04-16 00:00:00', NULL, NULL, 49750, 191, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (232, false, 3, '9481', 3, '2016-04-16 00:00:00', NULL, NULL, 20895, 191, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (233, false, 3, '9482', 3, '2016-03-17 00:00:00', NULL, NULL, 5200, 192, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (234, false, 4, NULL, NULL, NULL, NULL, NULL, 42008.8, 193, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (235, false, 4, NULL, NULL, NULL, NULL, NULL, 42008.8, 194, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (236, false, 3, '9483', 3, '2016-04-15 00:00:00', NULL, NULL, 10190.62, 195, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (237, false, 3, '9484', 3, '2016-03-17 00:00:00', NULL, NULL, 6233.92, 196, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (238, false, 3, '9485', 3, '2016-04-19 00:00:00', NULL, NULL, 33105.6, 197, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (239, false, 3, '9486', 3, '2016-03-22 00:00:00', NULL, NULL, 3146, 198, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (240, false, 7, NULL, NULL, NULL, NULL, NULL, 6248.93, 199, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (241, false, 4, NULL, NULL, NULL, NULL, NULL, 5426.85, 200, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (242, false, 3, '9492', 3, '2016-03-23 00:00:00', NULL, NULL, 3600, 201, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (243, false, 4, NULL, NULL, NULL, NULL, NULL, 35000, 202, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (244, false, 3, '9491', 3, '2016-03-18 00:00:00', NULL, NULL, 20000, 203, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (245, false, 3, '9490', 3, '2016-03-22 00:00:00', NULL, NULL, 16430, 204, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (246, false, 3, '9493', 3, '2016-03-22 00:00:00', NULL, NULL, 15246, 205, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (247, false, 3, '9494', 3, '2016-03-23 00:00:00', NULL, NULL, 3200, 206, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (248, false, 7, NULL, NULL, NULL, NULL, NULL, 4700, 207, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (254, false, 3, '9479', 3, '2016-03-24 00:00:00', NULL, NULL, 16275, 213, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (249, false, 3, '9495', 3, '2016-03-28 00:00:00', NULL, NULL, 1200, 208, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (251, false, 4, NULL, NULL, NULL, NULL, NULL, 10000, 210, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (253, false, 4, NULL, NULL, NULL, NULL, NULL, 22500, 212, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (223, true, 3, '9479', 3, '2016-03-24 00:00:00', NULL, NULL, 16275, 187, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (224, true, 3, '9480', 3, '2016-04-07 00:00:00', NULL, NULL, 30225, 187, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (266, false, 3, '9499', 3, '2016-04-04 00:00:00', NULL, NULL, 23232, 223, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (281, false, 3, '9529', 3, '2016-04-16 00:00:00', NULL, NULL, 8076.76, 233, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (286, false, 2, NULL, NULL, NULL, NULL, NULL, 520, 237, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (250, false, 4, NULL, NULL, NULL, NULL, NULL, 8276.4, 209, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (256, false, 4, NULL, NULL, NULL, NULL, NULL, 489.34, 214, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (265, false, 3, '9498', 3, '2016-04-08 00:00:00', NULL, NULL, 22710, 222, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (273, false, 4, NULL, NULL, NULL, NULL, NULL, 48400, 229, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (282, false, 3, '9527', 3, '2016-05-15 00:00:00', NULL, NULL, 20000, 234, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (283, false, 3, '9528', 3, '2016-05-23 00:00:00', NULL, NULL, 19807.06, 234, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (291, false, 4, NULL, NULL, NULL, NULL, NULL, 13594.17, 242, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (252, false, 4, NULL, NULL, NULL, NULL, NULL, 31400, 211, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (264, false, 4, NULL, NULL, NULL, NULL, NULL, 302.5, 221, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (277, false, 3, '9504', 3, '2016-03-29 00:00:00', NULL, NULL, 13000, 231, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (255, false, 3, '9480', 3, '2016-04-07 00:00:00', NULL, NULL, 30225, 213, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (257, false, 4, NULL, NULL, NULL, NULL, NULL, 70532.2, 215, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (261, false, 4, NULL, NULL, NULL, NULL, NULL, 1950.1, 218, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (262, false, 4, NULL, NULL, NULL, NULL, NULL, 20570, 219, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (268, false, 7, NULL, NULL, NULL, NULL, NULL, 94022.08, 225, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (271, false, 3, '9502', 3, '2016-03-31 00:00:00', NULL, NULL, 40000, 228, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (272, false, 3, '9503', 3, '2016-03-31 00:00:00', NULL, NULL, 30000, 228, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (288, false, 2, NULL, NULL, NULL, NULL, NULL, 25000, 239, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (289, false, 3, '9531', 3, '2016-05-04 00:00:00', NULL, NULL, 9369.03, 240, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (292, false, 4, NULL, NULL, NULL, NULL, NULL, 10362.44, 243, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (258, false, 3, '9497', 3, '2016-04-07 00:00:00', NULL, NULL, 35846.25, 216, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (267, false, 3, '9500', 3, '2016-04-11 00:00:00', NULL, NULL, 9196, 224, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (269, false, 2, NULL, NULL, NULL, NULL, NULL, 755.04, 226, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (278, false, 3, '9514', 3, '2016-03-22 00:00:00', NULL, NULL, 200000, 232, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (279, false, 3, '9512', 3, '2016-03-05 00:00:00', NULL, NULL, 400000, 232, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (280, false, 3, '9513', 3, '2016-03-13 00:00:00', NULL, NULL, 500000, 232, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (259, false, 3, '9496', 3, '2016-03-12 00:00:00', NULL, NULL, 45375, 217, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (260, false, 4, NULL, NULL, NULL, NULL, NULL, 45375, 217, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (263, false, 4, NULL, NULL, NULL, NULL, NULL, 5445, 220, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (270, false, 2, NULL, NULL, NULL, NULL, NULL, 1830, 227, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (276, true, 3, '9507', 3, '2016-03-15 00:00:00', NULL, NULL, 500000, 230, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (275, true, 3, '9506', 3, '2016-04-06 00:00:00', NULL, NULL, 400000, 230, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (274, true, 3, '9505', 3, '2016-04-01 00:00:00', NULL, NULL, 244055, 230, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (284, false, 4, NULL, NULL, NULL, NULL, NULL, 11256.98, 235, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (285, false, 2, NULL, NULL, NULL, NULL, NULL, 211.75, 236, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (287, false, 2, NULL, NULL, NULL, NULL, NULL, 24500, 238, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (290, false, 3, '9530', 3, '2016-04-08 00:00:00', NULL, NULL, 9860, 241, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (293, false, 3, '9533', 3, '2016-04-10 00:00:00', NULL, NULL, 3500, 244, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (294, false, 3, '9532', 3, '2016-04-05 00:00:00', NULL, NULL, 40888.32, 245, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (295, false, 4, NULL, NULL, NULL, NULL, NULL, 18150, 246, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (296, false, 4, NULL, NULL, NULL, NULL, NULL, 11374, 247, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (297, false, 4, NULL, NULL, NULL, NULL, NULL, 4840, 248, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (298, false, 4, NULL, NULL, NULL, NULL, NULL, 8225, 249, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (299, false, 4, NULL, NULL, NULL, NULL, NULL, 8995.14, 250, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (300, false, 3, '9535', 3, '2016-04-01 00:00:00', NULL, NULL, 23400, 251, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (301, false, 4, NULL, NULL, NULL, NULL, NULL, 12500, 252, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (302, false, 4, NULL, NULL, NULL, NULL, NULL, 22500, 253, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (303, false, 4, NULL, NULL, NULL, NULL, NULL, 10608.07, 254, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (304, false, 3, '9540', 3, '2016-04-20 00:00:00', NULL, NULL, 10504, 255, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (305, false, 7, NULL, NULL, NULL, NULL, NULL, 10360.02, 256, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (308, false, 3, '9545', 3, '2016-04-11 00:00:00', NULL, NULL, 907.5, 259, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (309, false, 3, '9547', 3, '2016-04-19 00:00:00', NULL, NULL, 62623.55, 260, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (310, false, 4, NULL, NULL, NULL, NULL, NULL, 4840, 261, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (311, false, 4, NULL, NULL, NULL, NULL, NULL, 3772.78, 262, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (312, false, 4, NULL, NULL, NULL, NULL, NULL, 18271, 263, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (313, false, 4, NULL, NULL, NULL, NULL, NULL, 14820.65, 264, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (314, false, 4, NULL, NULL, NULL, NULL, NULL, 7953.91, 265, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (315, false, 3, '9552', 3, '2016-04-26 00:00:00', NULL, NULL, 7507.02, 266, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (316, false, 3, '9550', 3, '2016-04-13 00:00:00', NULL, NULL, 14500, 267, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (317, false, 3, '9551', 3, '2016-04-13 00:00:00', NULL, NULL, 3500, 268, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (194, true, 3, '9450', 3, '2016-03-17 00:00:00', NULL, NULL, 5625, 165, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (318, false, 3, '9450', 3, '2016-03-17 00:00:00', NULL, NULL, 5625, 269, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (319, false, 3, '9549', 3, '2016-04-19 00:00:00', NULL, NULL, 11825, 270, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (320, false, 3, '9553', 3, '2016-04-25 00:00:00', NULL, NULL, 3583, 271, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (306, true, 3, '9544', 3, '2016-04-12 00:00:00', NULL, NULL, 29403, 257, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (321, false, 3, '9555', 3, '2016-04-18 00:00:00', NULL, NULL, 29403, 272, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (307, true, 3, '9543', 3, '2016-04-18 00:00:00', NULL, NULL, 151976, 258, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (326, true, 2, NULL, NULL, NULL, NULL, NULL, 2573, 273, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (325, true, 3, '9555', 3, '2016-04-18 00:00:00', NULL, NULL, 29403, 273, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (324, true, 3, '9558', 3, '2016-04-19 00:00:00', NULL, NULL, 40000, 273, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (323, true, 3, '9557', 3, '2016-04-19 00:00:00', NULL, NULL, 40000, 273, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (322, true, 3, '9556', 3, '2016-04-19 00:00:00', NULL, NULL, 40000, 273, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (327, false, 3, '9559', 3, '2016-04-19 00:00:00', NULL, NULL, 31129, 274, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (328, false, 3, '9558', 3, '2016-04-19 00:00:00', NULL, NULL, 40000, 274, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (329, false, 3, '9557', 3, '2016-04-19 00:00:00', NULL, NULL, 40000, 274, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (330, false, 3, '0556', 3, '2016-04-19 00:00:00', NULL, NULL, 40000, 274, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (331, false, 2, NULL, NULL, NULL, NULL, NULL, 847, 274, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (332, false, 3, '9561', 3, '2016-04-20 00:00:00', NULL, NULL, 48000, 275, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (333, false, 3, '9564', 3, '2016-04-18 00:00:00', NULL, NULL, 35000, 276, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (334, false, 3, '9562', 3, '2016-04-20 00:00:00', NULL, NULL, 36278.22, 277, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (335, false, 3, '9563', 3, '2016-04-20 00:00:00', NULL, NULL, 57844.05, 278, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (336, false, 4, NULL, NULL, NULL, NULL, NULL, 26136, 279, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (337, false, 4, NULL, NULL, NULL, NULL, NULL, 10285, 280, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (338, false, 4, NULL, NULL, NULL, NULL, NULL, 48400, 281, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (339, false, 4, NULL, NULL, NULL, NULL, NULL, 50820, 282, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (340, false, 4, NULL, NULL, NULL, NULL, NULL, 14520, 283, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (341, false, 3, '9565', 3, '2016-04-22 00:00:00', NULL, NULL, 13020, 284, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (342, false, 3, '9566', 3, '2016-05-30 00:00:00', NULL, NULL, 12777.6, 285, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (343, false, 3, '9567', 3, '2016-05-20 00:00:00', NULL, NULL, 22598.93, 286, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (345, false, 3, '9569', 3, '2016-04-25 00:00:00', NULL, NULL, 20000, 288, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (346, false, 3, '9570', 3, '2016-04-25 00:00:00', NULL, NULL, 7357.2, 289, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (347, false, 3, '9571', 3, '2016-04-25 00:00:00', NULL, NULL, 24000, 290, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (348, false, 3, '9572', 3, '2016-05-03 00:00:00', NULL, NULL, 20000, 291, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (349, false, 3, '9573', 3, '2016-05-03 00:00:00', NULL, NULL, 35000, 292, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (350, false, 3, '9574', 3, '2016-05-03 00:00:00', NULL, NULL, 15000, 293, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (351, false, 3, '9575', 3, '2016-04-25 00:00:00', NULL, NULL, 18150, 294, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (352, false, 4, NULL, NULL, NULL, NULL, NULL, 3388, 295, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (353, false, 3, '9577', 3, '2016-04-26 00:00:00', NULL, NULL, 26015, 296, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (354, false, 3, '9578', 3, '2016-04-25 00:00:00', NULL, NULL, 8550, 297, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (355, false, 3, '9579', 3, '2016-04-26 00:00:00', NULL, NULL, 25900, 298, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (356, false, 3, '9580', 3, '2016-04-28 00:00:00', NULL, NULL, 32266, 299, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (357, false, 3, '9581', 3, '2016-05-16 00:00:00', NULL, NULL, 32267, 299, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (358, false, 3, '9582', 3, '2016-05-30 00:00:00', NULL, NULL, 32267, 299, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (359, false, 4, NULL, NULL, NULL, NULL, NULL, 8080.74, 300, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (360, false, 4, NULL, NULL, NULL, NULL, NULL, 1190, 301, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (361, false, 4, NULL, NULL, NULL, NULL, NULL, 7500, 302, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (344, true, 3, '9568', 3, '2016-05-03 00:00:00', NULL, NULL, 13458, 287, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (362, false, 3, '9568', 3, '2016-05-03 00:00:00', NULL, NULL, 69165.19, 303, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (363, false, 3, '9583', 3, '2016-06-20 00:00:00', NULL, NULL, 61710, 304, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (364, false, 4, NULL, NULL, NULL, NULL, NULL, 40881.21, 305, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (365, false, 4, NULL, NULL, NULL, NULL, NULL, 13300, 306, NULL);
INSERT INTO pago (id, borrado, modo_pago_id, nro_cheque, banco_id, fecha_cheque, cobro_alternativo_id, cuenta_bancaria_id, importe, orden_pago_id, cuit_cuil) VALUES (366, false, 4, NULL, NULL, NULL, NULL, NULL, 1600, 307, NULL);


--
-- TOC entry 2339 (class 0 OID 0)
-- Dependencies: 194
-- Name: pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('pago_id_seq', 366, true);


--
-- TOC entry 2284 (class 0 OID 20056)
-- Dependencies: 215
-- Data for Name: pago_solicitud_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2340 (class 0 OID 0)
-- Dependencies: 216
-- Name: pago_solicitud_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('pago_solicitud_pago_id_seq', 1, false);


--
-- TOC entry 2265 (class 0 OID 19758)
-- Dependencies: 195
-- Data for Name: proveedor; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (58, false, 'Débora Daniela Rosales', '27-34852782-2', 42, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (61, false, 'Alvarez Gabriel', '20-24847016-0', 45, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (62, false, 'Marino Giselle Andrea', '27-25171232-3', 46, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (63, false, 'Sebastian Ricardo Coria', '20-28779292-2', 47, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (2, false, 'De Blasi Rodolfo Valentin', '20-16057959-6', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (3, false, 'Agustin Aranguren', NULL, NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (4, false, 'Wanda Reyes', NULL, NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (8, false, 'Alvaro Barros', NULL, NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (64, false, 'La agencia de representaciónes artístcas S.A.', '30-71228388-9', 48, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (22, false, 'Porto Jorge Demian', '20-18285156-7', 9, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (65, false, 'Jumbo Retail Argentina S.A.', '30-70877296-4', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (12, false, 'Ryan''s Travel S.A.', '30-66211901-2', 3, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (1, false, 'Ad Graphis Bureau Creativo S.R.L.', '30-68515636-5', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (9, false, 'Aerolineas Argentinas S.A.', '30-64140555-4', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (16, false, 'Limpiolux facility services', '30-54098462-6', 6, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (18, false, 'Ibarz Enrique Manuel', NULL, NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (19, false, 'Alberto y Walter Pianetti', '30-70781655-0', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (66, false, 'Anibal W. Fernandez', '23-16204052-9', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (23, false, 'Multi Group S.R.L.', '30-70872839-6', 10, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (24, false, 'Elena Hosmann', NULL, NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (25, false, 'Diego Martin Blejer', '20-23967436-5', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (27, false, 'CPDD S.R.L.', '30-71443643-7', 12, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (28, false, 'Leonidas Gaston P Yorio', '20-22680767-6', 13, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (29, false, 'Hector A. Ellena', '20-17873597-8', 14, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (30, false, 'Inalpi S.A', '30-70783085-5', 15, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (31, false, 'Mailcar S.R.L.', '30-70546336-7', 16, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (32, false, 'Servicios Portuarios S.A.', '30-60675453-8', 17, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (33, false, 'Master S.R.L.', '30-70992705-8', 18, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (34, false, 'Micheletti María', '27-93159206-3', 19, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (35, false, 'Sociedad Bomberos Voluntarios', '30-66913326-6', 20, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (11, false, 'Molo Eduardo Daniel', '20-92522406-6', 22, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (38, false, 'Andino Franco Alejandro', '20-29697901-6', 24, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (39, false, 'Gargiulo Sebastian Leonel', '20-31258588-0', 25, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (41, false, 'Rosales Roberto Gabriel', '20-21511441-5', 27, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (40, false, 'New Idesas Publicidad', '30-70960665-0', 26, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (6, false, 'Jomalu S.A', '30-69964932-1', 1, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (7, false, 'Frali S.A', '30-63221905-5', 2, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (90, false, 'Camara de la Fruta Ind. Mendoza', '30-53556949-1', 66, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (21, false, 'Maladi S.R.L', '30-71490514-3', 8, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (36, false, 'Newsale S.R.L.', '30-70831170-3', 21, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (20, false, 'Nueva Fcia Antonello S.C.S', '30-68638733-6', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (17, false, 'Comisión Asoc.Coop. Jardín de infantes', '20-30227363-5', 7, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (37, false, 'SOCVA Group S.R.L.', '33-70708830-9', 23, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (15, false, 'Star Golf S.R.L.', '30-70861171-5', 5, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (5, false, 'Trag S.R.L', '30-70910259-8', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (44, false, 'Giorgi Alberto Oscar', '30-60107990-5', 28, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (45, false, 'Macarron Lucas Matias', '24-25698886-7', 29, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (47, false, 'Avanzini German V.', '23-25312999-9', 31, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (48, false, 'Fernandez Hector Daniel', '20-13090134-5', 32, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (49, false, 'Roxana Eva Lombardi', '20-17749501-9', 33, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (51, false, 'Juan De Dios Giacalone', '20-27931715-8', 35, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (52, false, 'Camila Soledad Illa', '27-33045758-4', 36, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (54, false, 'Yasik S.R.L.', '30-71115117-2', 38, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (55, false, 'La casa de las cajas S.A.', '30-56845110-0', 39, 1);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (57, false, 'Adrian Eduardo Catania', '20-25558324-8', 41, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (69, false, 'Hoteles Sheraton de Argentina S.A.C.', '30-51717562-1', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (70, false, 'Quilez Daniel Marcelo', '20-12801488-9', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (72, false, 'Mariani Imaz Ulises', '20-27635683-7', 50, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (73, false, 'Astesiano y Cia', '33-52809402-9', 51, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (75, false, 'Juan Pablo Alvarez', '20-25677342-3', 53, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (76, false, 'María Jose Dulin', '20-20752074-9', 54, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (77, false, 'Pini Mariana Regina', '27-24949651-6', 55, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (78, false, 'Rudd Sergio Esteban', '20-13522556-9', 56, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (79, false, 'Guerra Adrián Pablo', '20-21535229-4', 57, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (80, false, 'Gonzalez Magali Florencia', '27-29317551-4', 58, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (81, false, 'Abasto Tucumano S.A.', '30-70938825-4', 59, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (82, false, 'Graciela M. Lazzoli', '27-16124790-7', 60, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (85, false, 'Pighi Marcela Alejandra', '27-31889251-8', 63, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (68, false, 'Adecco Recursos Humanos S.A.', '33-66181499-9', 49, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (56, false, 'María Laura Zambrano Paz', '27-28324910-2', 40, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (84, false, 'Kumasi S.A.', '30-71169402-8', 62, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (53, false, 'Tocbor S.A.', '30-71122768-3', 37, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (46, false, 'Winter Group S.R.L.', '33-71412740-9', 30, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (43, false, 'Gax S.A.', '30-70840291-1', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (71, false, 'Geo For S.R.L.', '30-69810723-1', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (13, false, 'Ricale Viajes S.R.L.', '33-60447810-9', 4, 25);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (87, false, 'Minini Horacio Conrado', '20-11634156-6', NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (88, false, 'Rivara Agustin', '20-22992012-0', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (26, false, 'Artículos Promocionales S.A.', '30-70717426-5', 11, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (59, false, 'Coop. Agrícola Ganadera de Saladillo Lda.', '30-53281113-5', 43, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (14, false, 'Licoprog S.A.', '30-71489503-2', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (92, false, 'Olcese Marcos Daniel', '20-25775769-3', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (93, false, 'San Pedro Resort S.A.', NULL, NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (94, false, 'Carlos Francisco Ferreyra Greco', NULL, NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (95, false, 'Nosiglia Pedro', '23-30255040-9', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (10, false, 'Cutrini Diego Hernan', '20-28866665-3', 67, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (89, true, 'Georgi Alberto Oscar', '20-21641732-2', 64, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (96, false, 'Nuevo Gema S.A.', '30-68831651-7', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (60, false, 'Diego Israel Jaime', '23-27271920-9', 44, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (98, false, 'Chocolate Colonial S.A.', '30-64279620-4', 68, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (100, false, 'Ismael Suterh', '20-25851247-3', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (102, false, 'Sergio Trepat Automóviles S.A.', '30-64533018-4', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (103, false, 'Soc. Rural del Nor-Este Santiagueño', '30-57122850-1', 69, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (67, false, 'Cristian Muller', NULL, NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (91, false, 'HOCO S.A.', '30-71094703-8', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (97, false, 'Fideicomiso Inmobiliario Villa Maria', '30-71003877-1', 73, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (42, false, 'Coto C.I.C.S.A.', '30-54808315-6', NULL, 19);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (83, false, 'Complejo las Lajitas S.A.', '30-68133115-4', 61, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (86, false, 'Lazzari Gloria Maria', '27-11644092-5', 87, 11);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (74, false, 'Natal S.A.', '30-69826241-5', 52, 25);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (99, false, 'Deheza S.A.I.C.F.', '30-51618667-0', NULL, 19);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (50, false, 'BASIL S.R.L.', '30-71462319-9', 34, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (104, false, 'Fabián Rende', '20-17604543-5', 70, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (105, false, 'Inversora del sur grupo S.A.', '30-71142054-8', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (101, false, 'Pulenta Maria Julieta', '27-29042768-7', NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (117, false, 'Luis Obdulio Manzino', '20-11126886-0', 71, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (120, false, 'Goulu', '20-17985166-1', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (122, false, 'María Guadalupe Correa', '23-28325789-4', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (123, false, 'Garbarino S.A.', '30-54008821-3', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (124, false, 'Rizzuto Luciano Augusto', '20-28321703-6', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (126, false, 'Solini Marina Leticia', '27-21732337-7', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (131, false, 'El muelle 2010 S.A.', '30-71141670-2', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (134, false, 'Mario Vallone', '20-11271829-0', 80, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (139, false, 'Sociedad Hotelera Villa María S.A.', '30-71050283-4', 85, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (130, false, 'Chicharito S.A.', '30-71479972-6', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (127, false, 'Ecoexist S.R.L.', '30-71438155-1', 77, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (132, false, 'Ecosan S.A.', '30-70769684-9', 78, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (106, false, 'KLP Emprendimientos S.A.', '30-70828209-6', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (107, true, 'Muller Cristian', '20-28422165-7', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (108, false, 'Sans souci sociedad anónima com inm fin y const', '30-61890385-7', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (109, false, 'Antelme Chantal Maria', '27-26420624-9', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (110, false, 'Gainza Eurnekian Alberto', '20-28078028-7', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (111, false, 'Alfertur S.R.L.', '30-56838729-1', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (112, false, 'Coggiola Marcelo Emilio', '20-16536729-5', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (113, false, 'Plaza Real S.A.', '30-69371703-1', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (114, false, 'Il prole S.R.L.', '30-71195068-7', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (115, false, 'CENCOSUD S.A.', '30-59036076-3', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (116, false, 'Yacht club Puerto Madero S.A.', '30-68897723-8', 74, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (125, false, 'Hernandez Carlos R. y Christian D. (S.H.)', '30-71171080-5', 76, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (128, false, 'Mercado Maria Luz', '27-32766951-1', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (129, false, 'Jockey Club A.C.', '30-52799077-3', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (133, false, 'Gimenez Viviana Maria Lujan', '27-27386573-5', 79, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (135, false, 'Rivas Norma Liliana', '27-16529116-1', 81, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (136, false, 'Pulire S.A.', '30-69762024-5', 82, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (137, false, 'Hotelería y negocios S.A.', '30-70993987-0', 83, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (138, false, 'Casinos del Litoral S.A.', '33-71163484-9', 84, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (140, false, 'Puerto la pista S.R.L.', '30-71427861-0', 86, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (141, false, 'Nestle Argentina S.A.', '30-54676404-0', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (142, false, 'Prosegur Activa Argentina S.A.', NULL, NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (143, false, 'Ruben Dario Baffetti', '20-24639936-1', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (144, false, 'Avantgarde Agencia S.R.L.', '30-71499755-2', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (145, false, 'Ivan Galkin Garcés', '20-23326796-2', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (118, false, 'Valverde Eduardo Jorge', '20-28341364-1', 72, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (146, false, 'EAT S.A.', '33-69661604-9', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (147, false, 'Prego Diego Martin', '20-25675530-1', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (148, false, 'Hector O. Hoffmann', '20-07371136-4', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (149, false, 'Bole Ariel', '20-22891554-9', 88, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (151, false, 'Ares German Amadeo', '20-21150726-9', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (153, false, 'Bacigaluppi Hnos S.A.', '30-55799098-0', 90, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (158, false, 'Moro Catering S.A.', '30-71106314-1', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (157, false, 'Carbonere Javier Carlos', '20-21850094-4', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (159, false, 'Jouffre Maria Belen', '27-30947159-3', 91, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (160, false, 'Fernando Luis Blasi', '20-22004812-9', 92, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (161, false, 'Taller de Premios S.R.L.', '33-71367170-9', 93, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (150, false, 'Tecno Depot S.A.', '30-71059635-9', 89, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (162, false, 'Ocaranza Maria Lorena', '27-25562385-6', 94, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (163, false, 'De Ignacio Donadeu', '23-32677435-9', 95, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (164, false, 'WHQ S.A.', '30-71240193-8', 96, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (165, false, 'Lavadero industrial norte S.A.', '30-71099288-2', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (166, false, 'Ines Sofia de los Santos', '27-26194503-2', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (167, false, 'Jaguary Mink S.A.', '30-70943614-3', 97, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (168, false, 'Enzo Pinto', '20-13782409-5', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (169, false, 'Producciones SAC S.A.', '30-70928416-5', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (170, false, 'Maria Clara Valente', '27-29479221-5', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (171, false, 'Ticket Band S.R.L.', '30-71216847-8', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (121, false, 'Goriziano Romina', '27-23772196-4', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (197, false, 'El fogon de los arrieros S.R.L.', '30-71198029-2', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (119, false, 'Quintero Eduardo Hugo', '20-16016184-2', NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (172, false, 'Via Bariloche S.A.', '30-64392215-7', 98, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (173, false, 'Alvaro G. Montalvo', '23-29319641-9', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (174, false, '1 Km 1 Sonrisa O.N.G.', '33-71459105-9', 99, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (175, false, 'Mega Baby S.A.', '30-60242217-4', NULL, 17);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (176, false, 'Fioretti Ariel Marcel', '20-23576682-6', 100, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (177, false, 'Albino Abel, Albino María Cecilia S.H.', '30-71056804-5', 101, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (178, false, 'Matias Wierna', '20-28157826-0', 102, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (179, false, 'Maria Di Battista', '23-11564523-4', 103, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (180, false, 'Verdino Silvia Andrea', '27-28622476-3', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (181, false, 'CMY S.R.L.', '30-71431012-3', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (182, false, 'MB S.A.', '30-63293786-1', 104, 13);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (183, false, 'Juan Carlos Trobato', '20-11625757-3', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (184, false, 'Vidium S.R.L.', '30-70941925-7', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (185, false, 'Ideas en Red S.A.', '30-71036308-7', 105, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (186, false, 'Creciendo S.A.', '33-69887318-9', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (187, false, 'Javier Santiago Di Blasi', '20-29117979-8', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (188, false, 'Dagotto Leonardo Dario', '24-27699157-0', NULL, 3);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (156, false, 'Pelc Lucas Sebastian', '20-26500531-5', NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (155, false, 'Paul Re', '20-29262113-3', NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (154, false, 'Alvarez Benedettini Jorge Luis', '20-18828795-7', NULL, 7);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (189, false, 'Havanna S.A.', '33-69723504-9', 106, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (190, false, 'Registros Agropecuarios S.R.L.', '30-71221925-0', 107, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (191, false, 'Camara de Industria y Com. Arg.-Alemana', '30-53163455-8', 108, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (192, false, 'Zarwadski Elena Juana', '27-04268930-6', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (193, false, 'Toledo Fernando Flavio', '20-21979242-6', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (194, false, 'Szalardi Szegvari Esteban Carlos', '23-07642045-9', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (195, false, 'Irigoyen Carlos Gustavo', '20-30226651-5', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (196, false, 'Melisa Martinez', '27-30408640-3', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (198, false, 'The asia holdings group S.A.', '30-70938456-9', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (200, false, 'Estudio Edison S.R.L.', '30-70831810-4', 110, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (201, false, 'SPF Logistica S.A.', '30-71370940-5', 111, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (199, false, 'Bonomi María Luz', '27-05758235-4', 109, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (202, false, 'Nicepeople S.R.L.', '30-71478117-7', 112, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (203, false, 'Makaji Tomas', '20-26096253-2', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (204, false, 'Larrea Amoros Gonzalo Matias', '20-24940459-5', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (152, false, 'BIGBOX S.A.', '30-71109155-2', 113, 25);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (205, false, 'Cecilia Crespi', '27-23732043-9', 114, 9);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (206, false, 'BTB Trading sur S.R.L.', '30-71409387-4', NULL, 5);
INSERT INTO proveedor (id, borrado, razon_social, cuit_cuil, cuenta_bancaria_id, mascara_modo_pago) VALUES (207, false, 'Hoteles y Gestión S.R.L.', '30-71509830-6', 115, 9);


--
-- TOC entry 2341 (class 0 OID 0)
-- Dependencies: 196
-- Name: proveedor_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('proveedor_id_seq', 207, true);


--
-- TOC entry 2267 (class 0 OID 19763)
-- Dependencies: 197
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (1, false, 'Dueño del sistema', 'Administrador');
INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (4, false, 'Desarrollador', 'Desarrollador');
INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (3, false, 'Solo puede ver cosas, pero no puede editar ni crear nada', 'Solo Lectura');
INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (5, false, 'Encargado de realizar Solicitudes de Pagos', 'Solicitante Pagos');
INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (6, false, 'Encargado de realizar Solicitudes de Facturas de Venta', 'Solicitante Facturas Ventas');


--
-- TOC entry 2342 (class 0 OID 0)
-- Dependencies: 198
-- Name: rol_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('rol_id_seq', 6, true);


--
-- TOC entry 2282 (class 0 OID 20035)
-- Dependencies: 212
-- Data for Name: solicitud_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2343 (class 0 OID 0)
-- Dependencies: 213
-- Name: solicitud_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('solicitud_pago_id_seq', 1, false);


--
-- TOC entry 2269 (class 0 OID 19784)
-- Dependencies: 199
-- Data for Name: tipo_cuenta; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO tipo_cuenta (id, borrado, nombre, descripcion) VALUES (1, false, 'Caja de Ahorro', NULL);
INSERT INTO tipo_cuenta (id, borrado, nombre, descripcion) VALUES (2, false, 'Cuenta Corriente', NULL);


--
-- TOC entry 2344 (class 0 OID 0)
-- Dependencies: 200
-- Name: tipo_cuenta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('tipo_cuenta_id_seq', 2, true);


--
-- TOC entry 2271 (class 0 OID 19792)
-- Dependencies: 201
-- Data for Name: tipo_factura; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (1, false, 'A', 'Factura Tipo A');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (2, false, 'B', 'Factura Tipo B');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (3, false, 'C', 'Factura Tipo C');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (4, false, 'Nota de Debito A', 'Nota de Debito A');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (5, false, 'Nota de Debito B', 'Nota de Debito B');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (6, false, 'Nota de Debito C', 'Nota de Debito C');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (7, false, 'Nota de Credito A', 'Nota de Credito A');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (8, false, 'Nota de Credito B', 'Nota de Credito B');
INSERT INTO tipo_factura (id, borrado, nombre, descripcion) VALUES (9, false, 'Nota de Credito C', 'Nota de Credito C');


--
-- TOC entry 2345 (class 0 OID 0)
-- Dependencies: 202
-- Name: tipo_factura_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('tipo_factura_id_seq', 9, true);


--
-- TOC entry 2273 (class 0 OID 19800)
-- Dependencies: 203
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (1, false, 'Lisio', '76d80224611fc919a5d54f0ff9fba446', 'pablo@facturaronline.com.ar', 'Pablo', '---', 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (6, true, 'Canitrot', '202cb962ac59075b964b07152d234b70', 'bc@trebol4.com', 'Bruno', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (12, true, 'Hosman', '202cb962ac59075b964b07152d234b70', 'eh@trebol4.com', 'Elena', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (9, true, 'Contador', '202cb962ac59075b964b07152d234b70', 'lc@trebol4.com', 'Lucas', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (2, false, 'Lisio', '202cb962ac59075b964b07152d234b70', 'al@trebol4.com', 'Angel', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (3, false, 'Brandan', '202cb962ac59075b964b07152d234b70', 'ab@trebol4.com', 'Angela', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (4, false, 'Badano', '202cb962ac59075b964b07152d234b70', 'abadano@trebol4.com', 'Augusto', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (5, false, 'Giordano', '202cb962ac59075b964b07152d234b70', 'bg@trebol4.com', 'Bianca', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (7, false, 'Gonzalez Ramos', '202cb962ac59075b964b07152d234b70', 'cgr@trebol4.com', 'Cecilia', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (8, false, 'Muller', '202cb962ac59075b964b07152d234b70', 'cm@trebol4.com', 'Christian', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (10, false, 'Gregorini', '202cb962ac59075b964b07152d234b70', 'dg@trebol4.com', 'Daniela', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (11, false, 'Maranesi', '202cb962ac59075b964b07152d234b70', 'dm@trebol4.com', 'Delfina', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (13, false, 'Brandan', '202cb962ac59075b964b07152d234b70', 'jb@trebol4.com', 'Juan', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (14, false, 'Montoto', '202cb962ac59075b964b07152d234b70', 'mmontoto@trebol4.com', 'Marcela', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (15, false, 'Re', '202cb962ac59075b964b07152d234b70', 'pr@trebol4.com', 'Paul', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (16, false, 'Moreira', '202cb962ac59075b964b07152d234b70', 'sm@trebol4.com', 'Santyago', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (17, false, 'Bordeu', '202cb962ac59075b964b07152d234b70', 'sb@trebol4.com', 'Sofía', NULL, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, activacion) VALUES (18, false, 'Reyes', '202cb962ac59075b964b07152d234b70', 'wr@trebol4.com', 'Wanda', NULL, 'activado');


--
-- TOC entry 2346 (class 0 OID 0)
-- Dependencies: 204
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('usuario_id_seq', 18, true);


--
-- TOC entry 2275 (class 0 OID 19986)
-- Dependencies: 205
-- Data for Name: usuario_rol; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (5, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (8, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (10, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (11, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (13, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (15, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (16, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (17, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (18, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (1, 4, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (5, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (8, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (10, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (11, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (13, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (15, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (16, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (17, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (18, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (5, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (8, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (10, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (11, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (13, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (15, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (17, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (18, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (6, 5, true);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (6, 6, true);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (12, 5, true);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (9, 5, true);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (4, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (4, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (7, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (7, 6, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (7, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (14, 5, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (14, 3, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (3, 1, false);
INSERT INTO usuario_rol (usuario_id, rol_id, borrado) VALUES (2, 1, false);


--
-- TOC entry 2052 (class 2606 OID 19828)
-- Name: banco_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY banco
    ADD CONSTRAINT banco_pkey PRIMARY KEY (id);


--
-- TOC entry 2054 (class 2606 OID 19830)
-- Name: caja_chica_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY caja_chica
    ADD CONSTRAINT caja_chica_pkey PRIMARY KEY (id);


--
-- TOC entry 2056 (class 2606 OID 19832)
-- Name: carga_hs_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY carga_hs
    ADD CONSTRAINT carga_hs_pkey PRIMARY KEY (id);


--
-- TOC entry 2090 (class 2606 OID 20012)
-- Name: cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id);


--
-- TOC entry 2058 (class 2606 OID 19834)
-- Name: cobro_alternativo_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY cobro_alternativo
    ADD CONSTRAINT cobro_alternativo_pkey PRIMARY KEY (id);


--
-- TOC entry 2060 (class 2606 OID 19836)
-- Name: cuenta_bancaria_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY cuenta_bancaria
    ADD CONSTRAINT cuenta_bancaria_pkey PRIMARY KEY (id);


--
-- TOC entry 2062 (class 2606 OID 19838)
-- Name: estado_factura_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY estado_factura_compra
    ADD CONSTRAINT estado_factura_pkey PRIMARY KEY (id);


--
-- TOC entry 2092 (class 2606 OID 20023)
-- Name: estado_solicitud_factura_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY estado_solicitud_pago
    ADD CONSTRAINT estado_solicitud_factura_pkey PRIMARY KEY (id);


--
-- TOC entry 2064 (class 2606 OID 19840)
-- Name: evento_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY evento
    ADD CONSTRAINT evento_pkey PRIMARY KEY (id);


--
-- TOC entry 2068 (class 2606 OID 19842)
-- Name: factura_orden_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY factura_compra_orden_pago
    ADD CONSTRAINT factura_orden_pago_pkey PRIMARY KEY (factura_compra_id, orden_pago_id);


--
-- TOC entry 2066 (class 2606 OID 19844)
-- Name: factura_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY factura_compra
    ADD CONSTRAINT factura_pkey PRIMARY KEY (id);


--
-- TOC entry 2094 (class 2606 OID 20034)
-- Name: factura_solicitud_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY factura_solicitud_pago
    ADD CONSTRAINT factura_solicitud_pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2070 (class 2606 OID 19846)
-- Name: modo_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY modo_pago
    ADD CONSTRAINT modo_pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2072 (class 2606 OID 19848)
-- Name: orden_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY orden_pago
    ADD CONSTRAINT orden_pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2074 (class 2606 OID 19850)
-- Name: pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2098 (class 2606 OID 20066)
-- Name: pago_solicitud_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY pago_solicitud_pago
    ADD CONSTRAINT pago_solicitud_pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2076 (class 2606 OID 19854)
-- Name: proveedor_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY proveedor
    ADD CONSTRAINT proveedor_pkey PRIMARY KEY (id);


--
-- TOC entry 2078 (class 2606 OID 19856)
-- Name: rol_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id);


--
-- TOC entry 2096 (class 2606 OID 20045)
-- Name: solicitud_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY solicitud_pago
    ADD CONSTRAINT solicitud_pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2080 (class 2606 OID 19864)
-- Name: tipo_cuenta_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY tipo_cuenta
    ADD CONSTRAINT tipo_cuenta_pkey PRIMARY KEY (id);


--
-- TOC entry 2082 (class 2606 OID 19866)
-- Name: tipo_factura_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY tipo_factura
    ADD CONSTRAINT tipo_factura_pkey PRIMARY KEY (id);


--
-- TOC entry 2084 (class 2606 OID 19868)
-- Name: usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- TOC entry 2086 (class 2606 OID 19870)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2088 (class 2606 OID 19991)
-- Name: usuario_rol_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY usuario_rol
    ADD CONSTRAINT usuario_rol_pkey PRIMARY KEY (usuario_id, rol_id);


--
-- TOC entry 2102 (class 2606 OID 19876)
-- Name: banco_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cuenta_bancaria
    ADD CONSTRAINT banco_fk FOREIGN KEY (banco_id) REFERENCES banco(id);


--
-- TOC entry 2112 (class 2606 OID 19881)
-- Name: banco_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT banco_fk FOREIGN KEY (banco_id) REFERENCES banco(id);


--
-- TOC entry 2123 (class 2606 OID 20088)
-- Name: cliente_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY solicitud_pago
    ADD CONSTRAINT cliente_fk FOREIGN KEY (cliente_id) REFERENCES cliente(id);


--
-- TOC entry 2113 (class 2606 OID 19886)
-- Name: cobro_alternativo_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT cobro_alternativo_fk FOREIGN KEY (cobro_alternativo_id) REFERENCES cobro_alternativo(id);


--
-- TOC entry 2128 (class 2606 OID 20093)
-- Name: cobro_alternativo_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago_solicitud_pago
    ADD CONSTRAINT cobro_alternativo_fk FOREIGN KEY (cobro_alternativo_id) REFERENCES cobro_alternativo(id);


--
-- TOC entry 2117 (class 2606 OID 19891)
-- Name: cuenta_bancaria_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY proveedor
    ADD CONSTRAINT cuenta_bancaria_fk FOREIGN KEY (cuenta_bancaria_id) REFERENCES cuenta_bancaria(id);


--
-- TOC entry 2100 (class 2606 OID 19896)
-- Name: cuenta_bancaria_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cobro_alternativo
    ADD CONSTRAINT cuenta_bancaria_fk FOREIGN KEY (cuenta_bancaria_id) REFERENCES cuenta_bancaria(id);


--
-- TOC entry 2114 (class 2606 OID 19901)
-- Name: cuenta_bancaria_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT cuenta_bancaria_fk FOREIGN KEY (cuenta_bancaria_id) REFERENCES cuenta_bancaria(id);


--
-- TOC entry 2129 (class 2606 OID 20098)
-- Name: cuenta_bancaria_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago_solicitud_pago
    ADD CONSTRAINT cuenta_bancaria_fk FOREIGN KEY (cuenta_bancaria_id) REFERENCES cuenta_bancaria(id);


--
-- TOC entry 2104 (class 2606 OID 19906)
-- Name: estado_factura_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_compra
    ADD CONSTRAINT estado_factura_fk FOREIGN KEY (estado_factura_compra_id) REFERENCES estado_factura_compra(id);


--
-- TOC entry 2124 (class 2606 OID 20103)
-- Name: estado_solicitud_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY solicitud_pago
    ADD CONSTRAINT estado_solicitud_pago_fk FOREIGN KEY (estado_solicitud_pago_id) REFERENCES estado_solicitud_pago(id);


--
-- TOC entry 2109 (class 2606 OID 19911)
-- Name: evento_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY orden_pago
    ADD CONSTRAINT evento_fk FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 2125 (class 2606 OID 20108)
-- Name: evento_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY solicitud_pago
    ADD CONSTRAINT evento_fk FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 2108 (class 2606 OID 20113)
-- Name: factura_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_compra_orden_pago
    ADD CONSTRAINT factura_compra_fk FOREIGN KEY (factura_compra_id) REFERENCES factura_compra(id);


--
-- TOC entry 2120 (class 2606 OID 20118)
-- Name: factura_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_solicitud_pago
    ADD CONSTRAINT factura_compra_fk FOREIGN KEY (factura_compra_id) REFERENCES factura_compra(id);


--
-- TOC entry 2115 (class 2606 OID 19921)
-- Name: modo_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT modo_pago_fk FOREIGN KEY (modo_pago_id) REFERENCES modo_pago(id);


--
-- TOC entry 2130 (class 2606 OID 20123)
-- Name: modo_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago_solicitud_pago
    ADD CONSTRAINT modo_pago_fk FOREIGN KEY (modo_pago_id) REFERENCES modo_pago(id);


--
-- TOC entry 2116 (class 2606 OID 19926)
-- Name: orden_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT orden_pago_fk FOREIGN KEY (orden_pago_id) REFERENCES orden_pago(id);


--
-- TOC entry 2107 (class 2606 OID 19931)
-- Name: orden_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_compra_orden_pago
    ADD CONSTRAINT orden_pago_fk FOREIGN KEY (orden_pago_id) REFERENCES orden_pago(id);


--
-- TOC entry 2101 (class 2606 OID 19941)
-- Name: proveedor_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cobro_alternativo
    ADD CONSTRAINT proveedor_fk FOREIGN KEY (proveedor_id) REFERENCES proveedor(id);


--
-- TOC entry 2105 (class 2606 OID 19946)
-- Name: proveedor_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_compra
    ADD CONSTRAINT proveedor_fk FOREIGN KEY (proveedor_id) REFERENCES proveedor(id);


--
-- TOC entry 2126 (class 2606 OID 20128)
-- Name: proveedor_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY solicitud_pago
    ADD CONSTRAINT proveedor_fk FOREIGN KEY (proveedor_id) REFERENCES proveedor(id);


--
-- TOC entry 2118 (class 2606 OID 20133)
-- Name: rol_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY usuario_rol
    ADD CONSTRAINT rol_fk FOREIGN KEY (rol_id) REFERENCES rol(id);


--
-- TOC entry 2111 (class 2606 OID 20051)
-- Name: solicitud_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY orden_pago
    ADD CONSTRAINT solicitud_pago_fk FOREIGN KEY (solicitud_pago_id) REFERENCES solicitud_pago(id);


--
-- TOC entry 2121 (class 2606 OID 20143)
-- Name: solicitud_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_solicitud_pago
    ADD CONSTRAINT solicitud_pago_fk FOREIGN KEY (solicitud_pago_id) REFERENCES solicitud_pago(id);


--
-- TOC entry 2131 (class 2606 OID 20148)
-- Name: solicitud_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago_solicitud_pago
    ADD CONSTRAINT solicitud_pago_fk FOREIGN KEY (solicitud_pago_id) REFERENCES solicitud_pago(id);


--
-- TOC entry 2103 (class 2606 OID 19976)
-- Name: tipo_cuenta_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cuenta_bancaria
    ADD CONSTRAINT tipo_cuenta_fk FOREIGN KEY (tipo_cuenta_id) REFERENCES tipo_cuenta(id);


--
-- TOC entry 2106 (class 2606 OID 19981)
-- Name: tipo_factura_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_compra
    ADD CONSTRAINT tipo_factura_fk FOREIGN KEY (tipo_factura_id) REFERENCES tipo_factura(id);


--
-- TOC entry 2122 (class 2606 OID 20153)
-- Name: tipo_factura_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_solicitud_pago
    ADD CONSTRAINT tipo_factura_fk FOREIGN KEY (tipo_factura_compra_id) REFERENCES tipo_factura(id);


--
-- TOC entry 2119 (class 2606 OID 20138)
-- Name: usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY usuario_rol
    ADD CONSTRAINT usuario_fk FOREIGN KEY (usuario_id) REFERENCES usuario(id);


--
-- TOC entry 2099 (class 2606 OID 19992)
-- Name: usuario_solicitante_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY caja_chica
    ADD CONSTRAINT usuario_solicitante_fk FOREIGN KEY (usuario_solicitante_id) REFERENCES usuario(id);


--
-- TOC entry 2110 (class 2606 OID 19997)
-- Name: usuario_solicitante_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY orden_pago
    ADD CONSTRAINT usuario_solicitante_fk FOREIGN KEY (usuario_solicitante_id) REFERENCES usuario(id);


--
-- TOC entry 2127 (class 2606 OID 20158)
-- Name: usuario_solicitante_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY solicitud_pago
    ADD CONSTRAINT usuario_solicitante_fk FOREIGN KEY (usuario_solicitante_id) REFERENCES usuario(id);


--
-- TOC entry 2292 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-04-22 16:20:21

--
-- PostgreSQL database dump complete
--

