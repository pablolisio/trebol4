--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.2
-- Dumped by pg_dump version 9.4.2
-- Started on 2015-12-18 10:55:12

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--DROP DATABASE "FacturarOnLineDB";
--
-- TOC entry 2218 (class 1262 OID 16662)
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
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2219 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 207 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2221 (class 0 OID 0)
-- Dependencies: 207
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 185 (class 1259 OID 16934)
-- Name: banco; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE banco (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255) NOT NULL
);


ALTER TABLE banco OWNER TO facturaronline;

--
-- TOC entry 184 (class 1259 OID 16932)
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
-- TOC entry 2222 (class 0 OID 0)
-- Dependencies: 184
-- Name: banco_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE banco_id_seq OWNED BY banco.id;


--
-- TOC entry 183 (class 1259 OID 16922)
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
-- TOC entry 182 (class 1259 OID 16920)
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
-- TOC entry 2223 (class 0 OID 0)
-- Dependencies: 182
-- Name: carga_hs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE carga_hs_id_seq OWNED BY carga_hs.id;


--
-- TOC entry 197 (class 1259 OID 17074)
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
-- TOC entry 196 (class 1259 OID 17072)
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
-- TOC entry 2224 (class 0 OID 0)
-- Dependencies: 196
-- Name: cobro_alternativo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE cobro_alternativo_id_seq OWNED BY cobro_alternativo.id;


--
-- TOC entry 193 (class 1259 OID 17043)
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
-- TOC entry 192 (class 1259 OID 17041)
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
-- TOC entry 2225 (class 0 OID 0)
-- Dependencies: 192
-- Name: cuenta_bancaria_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE cuenta_bancaria_id_seq OWNED BY cuenta_bancaria.id;


--
-- TOC entry 189 (class 1259 OID 16962)
-- Name: estado_factura; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE estado_factura (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255),
    descripcion character varying(255)
);


ALTER TABLE estado_factura OWNER TO facturaronline;

--
-- TOC entry 188 (class 1259 OID 16960)
-- Name: estado_factura_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE estado_factura_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE estado_factura_id_seq OWNER TO facturaronline;

--
-- TOC entry 2226 (class 0 OID 0)
-- Dependencies: 188
-- Name: estado_factura_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE estado_factura_id_seq OWNED BY estado_factura.id;


--
-- TOC entry 201 (class 1259 OID 17215)
-- Name: evento; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE evento (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    nombre character varying(255) NOT NULL
);


ALTER TABLE evento OWNER TO facturaronline;

--
-- TOC entry 200 (class 1259 OID 17213)
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
-- TOC entry 2227 (class 0 OID 0)
-- Dependencies: 200
-- Name: evento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE evento_id_seq OWNED BY evento.id;


--
-- TOC entry 205 (class 1259 OID 17362)
-- Name: factura; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE factura (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    fecha timestamp without time zone NOT NULL,
    mes_impositivo timestamp without time zone NOT NULL,
    proveedor_id integer NOT NULL,
    tipo character(1) NOT NULL,
    nro character varying(255) NOT NULL,
    subtotal numeric NOT NULL,
    iva numeric NOT NULL,
    perc_iva numeric,
    perc_iibb numeric,
    estado_factura_id integer NOT NULL
);


ALTER TABLE factura OWNER TO facturaronline;

--
-- TOC entry 204 (class 1259 OID 17360)
-- Name: factura_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE factura_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE factura_id_seq OWNER TO facturaronline;

--
-- TOC entry 2228 (class 0 OID 0)
-- Dependencies: 204
-- Name: factura_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE factura_id_seq OWNED BY factura.id;


--
-- TOC entry 206 (class 1259 OID 17381)
-- Name: factura_orden_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE factura_orden_pago (
    factura_id integer NOT NULL,
    orden_pago_id integer NOT NULL,
    borrado boolean NOT NULL
);


ALTER TABLE factura_orden_pago OWNER TO facturaronline;

--
-- TOC entry 172 (class 1259 OID 16692)
-- Name: gasto; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE gasto (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    detalle character varying(255) NOT NULL,
    mes date NOT NULL,
    monto numeric NOT NULL,
    sistema_id integer
);


ALTER TABLE gasto OWNER TO facturaronline;

--
-- TOC entry 173 (class 1259 OID 16698)
-- Name: gasto_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE gasto_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE gasto_id_seq OWNER TO facturaronline;

--
-- TOC entry 2229 (class 0 OID 0)
-- Dependencies: 173
-- Name: gasto_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE gasto_id_seq OWNED BY gasto.id;


--
-- TOC entry 187 (class 1259 OID 16951)
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
-- TOC entry 186 (class 1259 OID 16949)
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
-- TOC entry 2230 (class 0 OID 0)
-- Dependencies: 186
-- Name: modo_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE modo_pago_id_seq OWNED BY modo_pago.id;


--
-- TOC entry 199 (class 1259 OID 17142)
-- Name: orden_pago; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE orden_pago (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    fecha timestamp without time zone NOT NULL,
    nro character varying(255) NOT NULL,
    evento_id integer NOT NULL,
    concepto character varying(255),
    solicitado_por character varying(255)
);


ALTER TABLE orden_pago OWNER TO facturaronline;

--
-- TOC entry 198 (class 1259 OID 17140)
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
-- TOC entry 2231 (class 0 OID 0)
-- Dependencies: 198
-- Name: orden_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE orden_pago_id_seq OWNED BY orden_pago.id;


--
-- TOC entry 203 (class 1259 OID 17325)
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
    orden_pago_id integer NOT NULL
);


ALTER TABLE pago OWNER TO facturaronline;

--
-- TOC entry 202 (class 1259 OID 17323)
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
-- TOC entry 2232 (class 0 OID 0)
-- Dependencies: 202
-- Name: pago_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE pago_id_seq OWNED BY pago.id;


--
-- TOC entry 180 (class 1259 OID 16902)
-- Name: plan; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE plan (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    descripcion character varying(255),
    nombre_plan character varying(255) NOT NULL,
    precio numeric
);


ALTER TABLE plan OWNER TO facturaronline;

--
-- TOC entry 181 (class 1259 OID 16908)
-- Name: plan_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE plan_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE plan_id_seq OWNER TO facturaronline;

--
-- TOC entry 2233 (class 0 OID 0)
-- Dependencies: 181
-- Name: plan_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE plan_id_seq OWNED BY plan.id;


--
-- TOC entry 195 (class 1259 OID 17061)
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
-- TOC entry 194 (class 1259 OID 17059)
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
-- TOC entry 2234 (class 0 OID 0)
-- Dependencies: 194
-- Name: proveedor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE proveedor_id_seq OWNED BY proveedor.id;


--
-- TOC entry 174 (class 1259 OID 16737)
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
-- TOC entry 175 (class 1259 OID 16743)
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
-- TOC entry 2235 (class 0 OID 0)
-- Dependencies: 175
-- Name: rol_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE rol_id_seq OWNED BY rol.id;


--
-- TOC entry 176 (class 1259 OID 16745)
-- Name: sistema; Type: TABLE; Schema: public; Owner: facturaronline; Tablespace: 
--

CREATE TABLE sistema (
    id integer NOT NULL,
    borrado boolean NOT NULL,
    direccion character varying(255),
    meses_en_deuda character varying(255),
    administrador_id integer,
    plan_id integer
);


ALTER TABLE sistema OWNER TO facturaronline;

--
-- TOC entry 177 (class 1259 OID 16751)
-- Name: sistema_id_seq; Type: SEQUENCE; Schema: public; Owner: facturaronline
--

CREATE SEQUENCE sistema_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sistema_id_seq OWNER TO facturaronline;

--
-- TOC entry 2236 (class 0 OID 0)
-- Dependencies: 177
-- Name: sistema_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE sistema_id_seq OWNED BY sistema.id;


--
-- TOC entry 191 (class 1259 OID 16973)
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
-- TOC entry 190 (class 1259 OID 16971)
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
-- TOC entry 2237 (class 0 OID 0)
-- Dependencies: 190
-- Name: tipo_cuenta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE tipo_cuenta_id_seq OWNED BY tipo_cuenta.id;


--
-- TOC entry 178 (class 1259 OID 16753)
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
    rol_id integer NOT NULL,
    sistema_id integer,
    activacion character varying(255)
);


ALTER TABLE usuario OWNER TO facturaronline;

--
-- TOC entry 179 (class 1259 OID 16759)
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
-- TOC entry 2238 (class 0 OID 0)
-- Dependencies: 179
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: facturaronline
--

ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;


--
-- TOC entry 1999 (class 2604 OID 16937)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY banco ALTER COLUMN id SET DEFAULT nextval('banco_id_seq'::regclass);


--
-- TOC entry 1998 (class 2604 OID 16931)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY carga_hs ALTER COLUMN id SET DEFAULT nextval('carga_hs_id_seq'::regclass);


--
-- TOC entry 2005 (class 2604 OID 17077)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cobro_alternativo ALTER COLUMN id SET DEFAULT nextval('cobro_alternativo_id_seq'::regclass);


--
-- TOC entry 2003 (class 2604 OID 17046)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cuenta_bancaria ALTER COLUMN id SET DEFAULT nextval('cuenta_bancaria_id_seq'::regclass);


--
-- TOC entry 2001 (class 2604 OID 16965)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY estado_factura ALTER COLUMN id SET DEFAULT nextval('estado_factura_id_seq'::regclass);


--
-- TOC entry 2007 (class 2604 OID 17218)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY evento ALTER COLUMN id SET DEFAULT nextval('evento_id_seq'::regclass);


--
-- TOC entry 2009 (class 2604 OID 17365)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura ALTER COLUMN id SET DEFAULT nextval('factura_id_seq'::regclass);


--
-- TOC entry 1993 (class 2604 OID 16765)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY gasto ALTER COLUMN id SET DEFAULT nextval('gasto_id_seq'::regclass);


--
-- TOC entry 2000 (class 2604 OID 16954)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY modo_pago ALTER COLUMN id SET DEFAULT nextval('modo_pago_id_seq'::regclass);


--
-- TOC entry 2006 (class 2604 OID 17145)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY orden_pago ALTER COLUMN id SET DEFAULT nextval('orden_pago_id_seq'::regclass);


--
-- TOC entry 2008 (class 2604 OID 17328)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago ALTER COLUMN id SET DEFAULT nextval('pago_id_seq'::regclass);


--
-- TOC entry 1997 (class 2604 OID 16910)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY plan ALTER COLUMN id SET DEFAULT nextval('plan_id_seq'::regclass);


--
-- TOC entry 2004 (class 2604 OID 17064)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY proveedor ALTER COLUMN id SET DEFAULT nextval('proveedor_id_seq'::regclass);


--
-- TOC entry 1994 (class 2604 OID 16771)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY rol ALTER COLUMN id SET DEFAULT nextval('rol_id_seq'::regclass);


--
-- TOC entry 1995 (class 2604 OID 16772)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY sistema ALTER COLUMN id SET DEFAULT nextval('sistema_id_seq'::regclass);


--
-- TOC entry 2002 (class 2604 OID 16976)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY tipo_cuenta ALTER COLUMN id SET DEFAULT nextval('tipo_cuenta_id_seq'::regclass);


--
-- TOC entry 1996 (class 2604 OID 16773)
-- Name: id; Type: DEFAULT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);


--
-- TOC entry 2192 (class 0 OID 16934)
-- Dependencies: 185
-- Data for Name: banco; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO banco (id, borrado, nombre) VALUES (2, false, 'Banco Ciudad');
INSERT INTO banco (id, borrado, nombre) VALUES (3, false, 'CitiBank');
INSERT INTO banco (id, borrado, nombre) VALUES (1, false, 'Santander Rio');
INSERT INTO banco (id, borrado, nombre) VALUES (4, false, 'Galicia');


--
-- TOC entry 2239 (class 0 OID 0)
-- Dependencies: 184
-- Name: banco_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('banco_id_seq', 4, true);


--
-- TOC entry 2190 (class 0 OID 16922)
-- Dependencies: 183
-- Data for Name: carga_hs; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (3, false, 'Pagina de Carga de Horas', '2015-09-26 02:06:49.778', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (4, true, 'borrar', '2015-09-26 02:06:57.84', 1);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (2, false, 'Pagina Login', '2015-09-24 02:04:37.946', 2);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (5, false, 'Fin Pagina carga hs', '2015-09-27 00:23:28.887', 0.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (7, false, 'Arreglos varios y pagina Bancos', '2015-09-28 09:41:35.353', 1.3);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (8, false, 'Tablas fijas: Modo de Pago, Tipo Cuenta, Estados de Factura', '2015-10-02 10:15:56.894', 1.5);
INSERT INTO carga_hs (id, borrado, detalle, fecha, monto) VALUES (6, true, 'borrar', '2015-09-27 00:24:47.238', 12);
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


--
-- TOC entry 2240 (class 0 OID 0)
-- Dependencies: 182
-- Name: carga_hs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('carga_hs_id_seq', 52, true);


--
-- TOC entry 2204 (class 0 OID 17074)
-- Dependencies: 197
-- Data for Name: cobro_alternativo; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2241 (class 0 OID 0)
-- Dependencies: 196
-- Name: cobro_alternativo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('cobro_alternativo_id_seq', 1, true);


--
-- TOC entry 2200 (class 0 OID 17043)
-- Dependencies: 193
-- Data for Name: cuenta_bancaria; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2242 (class 0 OID 0)
-- Dependencies: 192
-- Name: cuenta_bancaria_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('cuenta_bancaria_id_seq', 1, true);


--
-- TOC entry 2196 (class 0 OID 16962)
-- Dependencies: 189
-- Data for Name: estado_factura; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO estado_factura (id, borrado, nombre, descripcion) VALUES (1, false, 'Pendiente de Pago', 'Pendiente de Pago');
INSERT INTO estado_factura (id, borrado, nombre, descripcion) VALUES (2, false, 'Cancelada', 'Cancelada');
INSERT INTO estado_factura (id, borrado, nombre, descripcion) VALUES (3, false, 'Pagada Parcial', 'Pagada Parcial');


--
-- TOC entry 2243 (class 0 OID 0)
-- Dependencies: 188
-- Name: estado_factura_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('estado_factura_id_seq', 3, true);


--
-- TOC entry 2208 (class 0 OID 17215)
-- Dependencies: 201
-- Data for Name: evento; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO evento (id, borrado, nombre) VALUES (1, false, 'Otros/Varios');
INSERT INTO evento (id, borrado, nombre) VALUES (2, false, 'Kids Tour');


--
-- TOC entry 2244 (class 0 OID 0)
-- Dependencies: 200
-- Name: evento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('evento_id_seq', 2, true);


--
-- TOC entry 2212 (class 0 OID 17362)
-- Dependencies: 205
-- Data for Name: factura; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2245 (class 0 OID 0)
-- Dependencies: 204
-- Name: factura_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('factura_id_seq', 1, true);


--
-- TOC entry 2213 (class 0 OID 17381)
-- Dependencies: 206
-- Data for Name: factura_orden_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2179 (class 0 OID 16692)
-- Dependencies: 172
-- Data for Name: gasto; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2246 (class 0 OID 0)
-- Dependencies: 173
-- Name: gasto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('gasto_id_seq', 1, true);


--
-- TOC entry 2194 (class 0 OID 16951)
-- Dependencies: 187
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
-- TOC entry 2247 (class 0 OID 0)
-- Dependencies: 186
-- Name: modo_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('modo_pago_id_seq', 7, true);


--
-- TOC entry 2206 (class 0 OID 17142)
-- Dependencies: 199
-- Data for Name: orden_pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2248 (class 0 OID 0)
-- Dependencies: 198
-- Name: orden_pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('orden_pago_id_seq', 1, true);


--
-- TOC entry 2210 (class 0 OID 17325)
-- Dependencies: 203
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2249 (class 0 OID 0)
-- Dependencies: 202
-- Name: pago_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('pago_id_seq', 1, true);


--
-- TOC entry 2187 (class 0 OID 16902)
-- Dependencies: 180
-- Data for Name: plan; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO plan (id, borrado, descripcion, nombre_plan, precio) VALUES (1, false, 'Es el plan más básico.', 'Básico', 0);


--
-- TOC entry 2250 (class 0 OID 0)
-- Dependencies: 181
-- Name: plan_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('plan_id_seq', 1, true);


--
-- TOC entry 2202 (class 0 OID 17061)
-- Dependencies: 195
-- Data for Name: proveedor; Type: TABLE DATA; Schema: public; Owner: facturaronline
--



--
-- TOC entry 2251 (class 0 OID 0)
-- Dependencies: 194
-- Name: proveedor_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('proveedor_id_seq', 1, true);


--
-- TOC entry 2181 (class 0 OID 16737)
-- Dependencies: 174
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (1, false, 'Dueño del sistema', 'Administrador');
INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (2, false, 'Personas encargadas de administrar las reservas de las canchas', 'Encargado');
INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (3, false, 'Persona que desea realizar una pre reserva para poder utilizar una cancha', 'Usuario Web');
INSERT INTO rol (id, borrado, descripcion, nombre_rol) VALUES (4, false, 'Desarrollador', 'Desarrollador');


--
-- TOC entry 2252 (class 0 OID 0)
-- Dependencies: 175
-- Name: rol_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('rol_id_seq', 4, true);


--
-- TOC entry 2183 (class 0 OID 16745)
-- Dependencies: 176
-- Data for Name: sistema; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO sistema (id, borrado, direccion, meses_en_deuda, administrador_id, plan_id) VALUES (1, false, '---', '20160105155300', 1, 1);


--
-- TOC entry 2253 (class 0 OID 0)
-- Dependencies: 177
-- Name: sistema_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('sistema_id_seq', 1, true);


--
-- TOC entry 2198 (class 0 OID 16973)
-- Dependencies: 191
-- Data for Name: tipo_cuenta; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO tipo_cuenta (id, borrado, nombre, descripcion) VALUES (1, false, 'Caja de Ahorro', NULL);
INSERT INTO tipo_cuenta (id, borrado, nombre, descripcion) VALUES (2, false, 'Cuenta Corriente', NULL);


--
-- TOC entry 2254 (class 0 OID 0)
-- Dependencies: 190
-- Name: tipo_cuenta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('tipo_cuenta_id_seq', 2, true);


--
-- TOC entry 2185 (class 0 OID 16753)
-- Dependencies: 178
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: facturaronline
--

INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, rol_id, sistema_id, activacion) VALUES (1, false, 'Lisio', '202cb962ac59075b964b07152d234b70', 'pablo@facturaronline.com.ar', 'Pablo', '---', 4, 1, 'activado');
INSERT INTO usuario (id, borrado, apellido, clave, email, nombre_o_razon_social, telefono, rol_id, sistema_id, activacion) VALUES (2, false, 'Lisio', '202cb962ac59075b964b07152d234b70', 'angel@trebol.com.ar', 'Angel', '--', 1, 1, 'activado');


--
-- TOC entry 2255 (class 0 OID 0)
-- Dependencies: 179
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: facturaronline
--

SELECT pg_catalog.setval('usuario_id_seq', 2, true);


--
-- TOC entry 2027 (class 2606 OID 16939)
-- Name: banco_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY banco
    ADD CONSTRAINT banco_pkey PRIMARY KEY (id);


--
-- TOC entry 2025 (class 2606 OID 16930)
-- Name: carga_hs_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY carga_hs
    ADD CONSTRAINT carga_hs_pkey PRIMARY KEY (id);


--
-- TOC entry 2039 (class 2606 OID 17079)
-- Name: cobro_alternativo_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY cobro_alternativo
    ADD CONSTRAINT cobro_alternativo_pkey PRIMARY KEY (id);


--
-- TOC entry 2035 (class 2606 OID 17048)
-- Name: cuenta_bancaria_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY cuenta_bancaria
    ADD CONSTRAINT cuenta_bancaria_pkey PRIMARY KEY (id);


--
-- TOC entry 2031 (class 2606 OID 16970)
-- Name: estado_factura_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY estado_factura
    ADD CONSTRAINT estado_factura_pkey PRIMARY KEY (id);


--
-- TOC entry 2043 (class 2606 OID 17220)
-- Name: evento_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY evento
    ADD CONSTRAINT evento_pkey PRIMARY KEY (id);


--
-- TOC entry 2049 (class 2606 OID 17385)
-- Name: factura_orden_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY factura_orden_pago
    ADD CONSTRAINT factura_orden_pago_pkey PRIMARY KEY (factura_id, orden_pago_id);


--
-- TOC entry 2047 (class 2606 OID 17370)
-- Name: factura_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY factura
    ADD CONSTRAINT factura_pkey PRIMARY KEY (id);


--
-- TOC entry 2011 (class 2606 OID 16787)
-- Name: gasto_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY gasto
    ADD CONSTRAINT gasto_pkey PRIMARY KEY (id);


--
-- TOC entry 2029 (class 2606 OID 16959)
-- Name: modo_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY modo_pago
    ADD CONSTRAINT modo_pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2041 (class 2606 OID 17147)
-- Name: orden_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY orden_pago
    ADD CONSTRAINT orden_pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2045 (class 2606 OID 17333)
-- Name: pago_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT pago_pkey PRIMARY KEY (id);


--
-- TOC entry 2023 (class 2606 OID 16912)
-- Name: plan_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY plan
    ADD CONSTRAINT plan_pkey PRIMARY KEY (id);


--
-- TOC entry 2037 (class 2606 OID 17066)
-- Name: proveedor_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY proveedor
    ADD CONSTRAINT proveedor_pkey PRIMARY KEY (id);


--
-- TOC entry 2013 (class 2606 OID 16801)
-- Name: rol_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id);


--
-- TOC entry 2015 (class 2606 OID 16803)
-- Name: sistema_administrador_id_key; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY sistema
    ADD CONSTRAINT sistema_administrador_id_key UNIQUE (administrador_id);


--
-- TOC entry 2017 (class 2606 OID 16805)
-- Name: sistema_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY sistema
    ADD CONSTRAINT sistema_pkey PRIMARY KEY (id);


--
-- TOC entry 2033 (class 2606 OID 16981)
-- Name: tipo_cuenta_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY tipo_cuenta
    ADD CONSTRAINT tipo_cuenta_pkey PRIMARY KEY (id);


--
-- TOC entry 2019 (class 2606 OID 16807)
-- Name: usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- TOC entry 2021 (class 2606 OID 16809)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: facturaronline; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2051 (class 2606 OID 16810)
-- Name: administrador_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY sistema
    ADD CONSTRAINT administrador_fk FOREIGN KEY (administrador_id) REFERENCES usuario(id);


--
-- TOC entry 2055 (class 2606 OID 17049)
-- Name: banco_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cuenta_bancaria
    ADD CONSTRAINT banco_fk FOREIGN KEY (banco_id) REFERENCES banco(id);


--
-- TOC entry 2061 (class 2606 OID 17334)
-- Name: banco_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT banco_fk FOREIGN KEY (banco_id) REFERENCES banco(id);


--
-- TOC entry 2062 (class 2606 OID 17339)
-- Name: cobro_alternativo_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT cobro_alternativo_fk FOREIGN KEY (cobro_alternativo_id) REFERENCES cobro_alternativo(id);


--
-- TOC entry 2057 (class 2606 OID 17067)
-- Name: cuenta_bancaria_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY proveedor
    ADD CONSTRAINT cuenta_bancaria_fk FOREIGN KEY (cuenta_bancaria_id) REFERENCES cuenta_bancaria(id);


--
-- TOC entry 2059 (class 2606 OID 17085)
-- Name: cuenta_bancaria_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cobro_alternativo
    ADD CONSTRAINT cuenta_bancaria_fk FOREIGN KEY (cuenta_bancaria_id) REFERENCES cuenta_bancaria(id);


--
-- TOC entry 2065 (class 2606 OID 17354)
-- Name: cuenta_bancaria_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT cuenta_bancaria_fk FOREIGN KEY (cuenta_bancaria_id) REFERENCES cuenta_bancaria(id);


--
-- TOC entry 2066 (class 2606 OID 17371)
-- Name: estado_factura_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura
    ADD CONSTRAINT estado_factura_fk FOREIGN KEY (estado_factura_id) REFERENCES estado_factura(id);


--
-- TOC entry 2060 (class 2606 OID 17229)
-- Name: evento_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY orden_pago
    ADD CONSTRAINT evento_fk FOREIGN KEY (evento_id) REFERENCES evento(id);


--
-- TOC entry 2068 (class 2606 OID 17386)
-- Name: factura_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_orden_pago
    ADD CONSTRAINT factura_fk FOREIGN KEY (factura_id) REFERENCES factura(id);


--
-- TOC entry 2063 (class 2606 OID 17344)
-- Name: modo_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT modo_pago_fk FOREIGN KEY (modo_pago_id) REFERENCES modo_pago(id);


--
-- TOC entry 2064 (class 2606 OID 17349)
-- Name: orden_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY pago
    ADD CONSTRAINT orden_pago_fk FOREIGN KEY (orden_pago_id) REFERENCES orden_pago(id);


--
-- TOC entry 2069 (class 2606 OID 17391)
-- Name: orden_pago_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura_orden_pago
    ADD CONSTRAINT orden_pago_fk FOREIGN KEY (orden_pago_id) REFERENCES orden_pago(id);


--
-- TOC entry 2052 (class 2606 OID 16913)
-- Name: plan_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY sistema
    ADD CONSTRAINT plan_fk FOREIGN KEY (plan_id) REFERENCES plan(id);


--
-- TOC entry 2058 (class 2606 OID 17080)
-- Name: proveedor_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cobro_alternativo
    ADD CONSTRAINT proveedor_fk FOREIGN KEY (proveedor_id) REFERENCES proveedor(id);


--
-- TOC entry 2067 (class 2606 OID 17376)
-- Name: proveedor_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY factura
    ADD CONSTRAINT proveedor_fk FOREIGN KEY (proveedor_id) REFERENCES proveedor(id);


--
-- TOC entry 2053 (class 2606 OID 16865)
-- Name: rol_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT rol_fk FOREIGN KEY (rol_id) REFERENCES rol(id);


--
-- TOC entry 2050 (class 2606 OID 16870)
-- Name: sistema_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY gasto
    ADD CONSTRAINT sistema_fk FOREIGN KEY (sistema_id) REFERENCES sistema(id);


--
-- TOC entry 2054 (class 2606 OID 16875)
-- Name: sistema_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT sistema_fk FOREIGN KEY (sistema_id) REFERENCES sistema(id);


--
-- TOC entry 2056 (class 2606 OID 17054)
-- Name: tipo_cuenta_fk; Type: FK CONSTRAINT; Schema: public; Owner: facturaronline
--

ALTER TABLE ONLY cuenta_bancaria
    ADD CONSTRAINT tipo_cuenta_fk FOREIGN KEY (tipo_cuenta_id) REFERENCES tipo_cuenta(id);


--
-- TOC entry 2220 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-12-18 10:55:13

--
-- PostgreSQL database dump complete
--

