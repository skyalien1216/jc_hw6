
CREATE TABLE product (
	code serial,
	name character varying NOT NULL,
	CONSTRAINT product_pk PRIMARY KEY (code)
);

CREATE TABLE invoice (
	id serial,
	date DATE NOT NULL,
	organization_id integer NOT NULL,
	CONSTRAINT invoice_pk PRIMARY KEY (id)
);

CREATE TABLE organization (
	inn serial,
	name character varying,
	bank_account character varying,
	CONSTRAINT organization_pk PRIMARY KEY (inn)
);

CREATE TABLE invoice_positions (
	id serial,
	invoice_id integer NOT NULL,
	price integer NOT NULL,
	amount integer NOT NULL,
	product_id integer NOT NULL,
	CONSTRAINT invoice_positions_pk PRIMARY KEY (id)
);

ALTER TABLE invoice ADD CONSTRAINT invoice_fk0 FOREIGN KEY (organization_id) REFERENCES organization(inn);
ALTER TABLE invoice_positions ADD CONSTRAINT invoice_positions_fk2 FOREIGN KEY (product_id) REFERENCES product(code);
ALTER TABLE invoice_positions ADD CONSTRAINT invoice_positions_fk3 FOREIGN KEY (invoice_id) REFERENCES invoice(id);





