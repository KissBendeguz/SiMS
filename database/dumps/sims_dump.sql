--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2 (Debian 15.2-1.pgdg110+1)
-- Dumped by pg_dump version 15.2 (Debian 15.2-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: business_user_map; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.business_user_map (
    business_id integer NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE public.business_user_map OWNER TO root;

--
-- Name: businesses; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.businesses (
    id integer NOT NULL,
    user_id integer,
    business_registration_date timestamp(6) without time zone,
    sims_registration_date timestamp(6) without time zone,
    head_quarters character varying(255),
    name character varying(255),
    tax_number character varying(255)
);


ALTER TABLE public.businesses OWNER TO root;

--
-- Name: businesses_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.businesses_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.businesses_seq OWNER TO root;

--
-- Name: inventories; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.inventories (
    business_id integer,
    id integer NOT NULL,
    address character varying(255),
    manager_email character varying(255),
    manager_name character varying(255),
    manager_phone character varying(255),
    name character varying(255)
);


ALTER TABLE public.inventories OWNER TO root;

--
-- Name: inventories_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.inventories_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.inventories_seq OWNER TO root;

--
-- Name: inventory_product_map; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.inventory_product_map (
    inventory_id integer NOT NULL,
    product_id integer NOT NULL
);


ALTER TABLE public.inventory_product_map OWNER TO root;

--
-- Name: product_property_map; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.product_property_map (
    product_id integer NOT NULL,
    property character varying(255),
    property_key character varying(255) NOT NULL
);


ALTER TABLE public.product_property_map OWNER TO root;

--
-- Name: products; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.products (
    added_by_id integer,
    id integer NOT NULL,
    inventory_id integer,
    quantity double precision NOT NULL,
    added_to_inventory timestamp(6) without time zone,
    category character varying(255),
    item_number character varying(255),
    name character varying(255),
    unit character varying(255)
);


ALTER TABLE public.products OWNER TO root;

--
-- Name: products_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.products_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.products_seq OWNER TO root;

--
-- Name: users; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.users (
    gender smallint,
    id integer NOT NULL,
    date_of_birth timestamp(6) without time zone,
    citizenship character varying(255),
    email character varying(255),
    firstname character varying(255),
    home_address character varying(255),
    identity_card_number character varying(255),
    lastname character varying(255),
    password character varying(255),
    phone_number character varying(255),
    place_of_birth character varying(255),
    social_security_number character varying(255),
    CONSTRAINT users_gender_check CHECK (((gender >= 0) AND (gender <= 3)))
);


ALTER TABLE public.users OWNER TO root;

--
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_seq OWNER TO root;

--
-- Data for Name: business_user_map; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.business_user_map (business_id, user_id) FROM stdin;
\.


--
-- Data for Name: businesses; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.businesses (id, user_id, business_registration_date, sims_registration_date, head_quarters, name, tax_number) FROM stdin;
\.


--
-- Data for Name: inventories; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.inventories (business_id, id, address, manager_email, manager_name, manager_phone, name) FROM stdin;
\.


--
-- Data for Name: inventory_product_map; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.inventory_product_map (inventory_id, product_id) FROM stdin;
\.


--
-- Data for Name: product_property_map; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.product_property_map (product_id, property, property_key) FROM stdin;
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.products (added_by_id, id, inventory_id, quantity, added_to_inventory, category, item_number, name, unit) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.users (gender, id, date_of_birth, citizenship, email, firstname, home_address, identity_card_number, lastname, password, phone_number, place_of_birth, social_security_number) FROM stdin;
\.


--
-- Name: businesses_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.businesses_seq', 1, false);


--
-- Name: inventories_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.inventories_seq', 1, false);


--
-- Name: products_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.products_seq', 1, false);


--
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.users_seq', 1, false);


--
-- Name: business_user_map business_user_map_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.business_user_map
    ADD CONSTRAINT business_user_map_pkey PRIMARY KEY (business_id, user_id);


--
-- Name: businesses businesses_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.businesses
    ADD CONSTRAINT businesses_pkey PRIMARY KEY (id);


--
-- Name: inventories inventories_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.inventories
    ADD CONSTRAINT inventories_pkey PRIMARY KEY (id);


--
-- Name: inventory_product_map inventory_product_map_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.inventory_product_map
    ADD CONSTRAINT inventory_product_map_pkey PRIMARY KEY (inventory_id, product_id);


--
-- Name: inventory_product_map inventory_product_map_product_id_key; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.inventory_product_map
    ADD CONSTRAINT inventory_product_map_product_id_key UNIQUE (product_id);


--
-- Name: product_property_map product_property_map_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.product_property_map
    ADD CONSTRAINT product_property_map_pkey PRIMARY KEY (product_id, property_key);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: products fk6bxocinrsauuylaj9p19jl1h8; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk6bxocinrsauuylaj9p19jl1h8 FOREIGN KEY (inventory_id) REFERENCES public.inventories(id);


--
-- Name: products fk6s0wjfaj3urtfajvv3lap5qtf; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk6s0wjfaj3urtfajvv3lap5qtf FOREIGN KEY (added_by_id) REFERENCES public.users(id);


--
-- Name: business_user_map fk76q1onfrig6gh4rc435emirau; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.business_user_map
    ADD CONSTRAINT fk76q1onfrig6gh4rc435emirau FOREIGN KEY (business_id) REFERENCES public.businesses(id);


--
-- Name: inventories fk7vt5ueo57co869193okf7ns02; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.inventories
    ADD CONSTRAINT fk7vt5ueo57co869193okf7ns02 FOREIGN KEY (business_id) REFERENCES public.businesses(id);


--
-- Name: product_property_map fk7xk461hy6hxad89iqdd4chx7q; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.product_property_map
    ADD CONSTRAINT fk7xk461hy6hxad89iqdd4chx7q FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: businesses fkg8wf081dyjc8mwodmg5mairv6; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.businesses
    ADD CONSTRAINT fkg8wf081dyjc8mwodmg5mairv6 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: inventory_product_map fkljtncvyd90x31ofyrgkojn04s; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.inventory_product_map
    ADD CONSTRAINT fkljtncvyd90x31ofyrgkojn04s FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: inventory_product_map fkn4t2v918od41dk59eq57g4lul; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.inventory_product_map
    ADD CONSTRAINT fkn4t2v918od41dk59eq57g4lul FOREIGN KEY (inventory_id) REFERENCES public.inventories(id);


--
-- Name: business_user_map fktpcv2ua5x8jbx2e9685oup8al; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.business_user_map
    ADD CONSTRAINT fktpcv2ua5x8jbx2e9685oup8al FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

