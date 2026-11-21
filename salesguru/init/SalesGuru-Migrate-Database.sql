\c salesguru
BEGIN;

ALTER TABLE public.items
    ALTER COLUMN cost   TYPE numeric(12,2) USING round(cost::numeric, 2),
    ALTER COLUMN retail TYPE numeric(12,2) USING round(retail::numeric, 2);

ALTER TABLE public.line_items
    ALTER COLUMN discount TYPE numeric(5,4) USING round(discount::numeric, 4);

ALTER TABLE public.line_items
    ADD CONSTRAINT line_qty_positive CHECK (qty > 0),
    ADD CONSTRAINT line_discount_range CHECK (discount BETWEEN 0 AND 1);

ALTER TABLE public.invoice
    ALTER COLUMN customer_number   TYPE integer,
    ALTER COLUMN representative_id TYPE integer;

ALTER TABLE public.invoice
    ADD CONSTRAINT invoice_company_fk FOREIGN KEY (customer_number)
        REFERENCES public.company (customer_number),
    ADD CONSTRAINT invoice_rep_fk FOREIGN KEY (representative_id)
        REFERENCES public.representative (representative_id);

ALTER TABLE public.line_items
    ADD CONSTRAINT line_invoice_fk FOREIGN KEY (invoice_number)
        REFERENCES public.invoice (invoice_number),
    ADD CONSTRAINT line_item_fk FOREIGN KEY (item_number)
        REFERENCES public.items (item_number);

ALTER TABLE public.representative VALIDATE CONSTRAINT customer_number_key;

COMMIT;
