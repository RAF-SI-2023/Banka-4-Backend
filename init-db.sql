CREATE SCHEMA IF NOT EXISTS berza_schema;
CREATE SCHEMA IF NOT EXISTS user_schema;
CREATE SCHEMA IF NOT EXISTS banka_schema;

CREATE OR REPLACE FUNCTION kupi_future_contract(radnik_id NUMERIC, future_contract_id NUMERIC, broj_racuna_id NUMERIC) RETURNS VOID AS $$
DECLARE 
    dailySpentPlusPrice NUMERIC;
    totalLimit NUMERIC;
    contractPrice NUMERIC;
balance NUMERIC;
isSupervisor BOOLEAN;
isApprovalFlag BOOLEAN;
kupac NUMERIC;
firmaId NUMERIC;
settlementDate NUMERIC;

radnikEmail VARCHAR;
radnikIme VARCHAR;
radnikPrezime VARCHAR;
brojTelefona VARCHAR;

BEGIN
    SELECT daily_spent,daily_limit,supervisor,approval_flag,firma_id INTO dailySpentPlusPrice,totalLimit,isSupervisor,isApprovalFlag,firmaId FROM user_schema.radnik WHERE "id" = radnik_id FOR UPDATE;
    SELECT price,kupac_id,settlement_date INTO contractPrice,kupac,settlementDate FROM berza_schema.futures_contract WHERE "id" = future_contract_id FOR UPDATE;
SELECT raspolozivo_stanje INTO balance FROM banka_schema.tekuci_racun WHERE "broj_racuna" = broj_racuna_id FOR UPDATE;

IF(kupac IS NOT NULL) THEN
RAISE EXCEPTION 'Future contract je vec kupljen';
END IF;
IF(CURRENT_DATE > to_timestamp(settlementDate / 1000)) THEN
RAISE EXCEPTION 'Future contract je istekao';
END IF;
    -- Izračunavanje ukupnog dnevnog troška plus cene future contracta
    dailySpentPlusPrice := dailySpentPlusPrice + contractPrice;

    -- Provera da li je ukupni dnevni trošak manji od dnevnog limita
    IF (contractPrice <= balance) THEN
        -- Provera da li ima dovoljno raspoloživog novca na tekucem racunu za kupovinu future contracta
        IF((isSupervisor IS TRUE OR (dailySpentPlusPrice <= totalLimit AND isApprovalFlag IS FALSE))) THEN
UPDATE banka_schema.tekuci_racun
SET "raspolozivo_stanje" = "raspolozivo_stanje" - contractPrice
WHERE "broj_racuna" = broj_racuna_id;

-- Ažuriranje dailySpent radnika
UPDATE user_schema.radnik SET "daily_spent" = "daily_spent" + contractPrice WHERE "id" = radnik_id;

-- Ažuriranje kupca u future contractu
UPDATE berza_schema.futures_contract SET "kupac_id" = firmaId,"racun_id"=broj_racuna_id WHERE "id" = future_contract_id;
        ELSE
SELECT email,ime,prezime,broj_telefona INTO radnikEmail,radnikIme,radnikPrezime,brojTelefona FROM user_schema.radnik WHERE "id" = radnik_id;
INSERT INTO berza_schema.future_contract_request (firma_id,futures_contract_id,racun_id,radnik_id,broj_telefona,email,ime,prezime,request_status)
VALUES (firmaId,future_contract_id,broj_racuna_id,radnik_id,brojTelefona,radnikEmail,radnikIme,radnikPrezime,'NOT_APPROVED');
   RAISE NOTICE 'Potrebno odobrenje za transakciju';
        -- Potvrda transakcije
END IF;
    ELSE
RAISE EXCEPTION 'Nema dovoljno novca na racunu.';
END IF;
END $$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION approve(contract_id NUMERIC, supervisor_id NUMERIC) RETURNS VOID AS $$
DECLARE 
balance NUMERIC; -- iskorisceno
kupac NUMERIC; -- iskorisceno
firmaId NUMERIC;
settlementDate NUMERIC; --iskorisceno
contractPrice NUMERIC; -- iskorisceno

racunId NUMERIC; -- iskorisceno
radnikId NUMERIC; -- iskorisceno
radnik_firma_id NUMERIC; -- iskorisceno
requestStatus VARCHAR; -- iskorisceno
futureContractId NUMERIC;

BEGIN
SELECT request_status,firma_id,settlement_date,kupac_id,price,racun_id,radnik_id,futures_contract_id 
INTO requestStatus,radnik_firma_id,settlementDate,kupac,contractPrice,racunId,radnikId,futureContractId
FROM berza_schema.future_contract_request fcr JOIN berza_schema.futures_contract fc
ON (fcr.futures_contract_id = fc.id)
WHERE fcr.futures_contract_id = contract_id
FOR UPDATE;

SELECT raspolozivo_stanje INTO balance FROM banka_schema.tekuci_racun 
WHERE "broj_racuna" = racunId FOR UPDATE;

SELECT firma_id INTO firmaId FROM user_schema.radnik WHERE id = supervisor_id FOR UPDATE;

IF(kupac IS NOT NULL) THEN
RAISE EXCEPTION 'Future contract je vec kupljen';
END IF;

IF(CURRENT_DATE > to_timestamp(settlementDate / 1000)) THEN
RAISE EXCEPTION 'Future contract je istekao';
END IF;

IF(firmaId <> radnik_firma_id) THEN
RAISE EXCEPTION 'Ovaj zahtev nije za vašu firmu!';
END IF;

IF(requestStatus <> 'NOT_APPROVED') THEN
RAISE EXCEPTION 'Vec je obradjen zahtev';
END IF;
    -- Provera da li je ukupni dnevni trošak manji od dnevnog limita
    IF (contractPrice <= balance) THEN
        -- Provera da li ima dovoljno raspoloživog novca na tekucem racunu za kupovinu future contracta
UPDATE banka_schema.tekuci_racun
SET "raspolozivo_stanje" = "raspolozivo_stanje" - contractPrice
WHERE "broj_racuna" = racunId;

-- Ažuriranje dailySpent radnika
UPDATE user_schema.radnik SET "daily_spent" = "daily_spent" + contractPrice WHERE "id" = radnikId;

-- Ažuriranje kupca u future contractu
UPDATE berza_schema.futures_contract SET "kupac_id" = firmaId,"racun_id"=racunId WHERE "id" = futureContractId;
    UPDATE berza_schema.future_contract_request SET "request_status" = 'APPROVED' WHERE "id" = contract_id;
ELSE
RAISE EXCEPTION 'Nema dovoljno novca na racunu.';
END IF;
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION obrada_transakcije(brojRacunaUplatioca NUMERIC,brojRacunaPrimaoca NUMERIC,
						iznosUplate NUMERIC,iznosPrimaocu NUMERIC) RETURNS BOOLEAN AS $$
DECLARE
aktivan_uplatilac BOOLEAN;
aktivan_primalac BOOLEAN;
rezervisanaSredstva NUMERIC;
valuta_uplatilac VARCHAR;
valuta_primalac VARCHAR;
stanjeMenjacnicaPrimalac NUMERIC;
postojiValutaUplatilac VARCHAR;
postojiValutaPrimalac VARCHAR;
BEGIN                                                                         
	SELECT aktivan,currency,stanje - raspolozivo_stanje 
	INTO aktivan_uplatilac,valuta_uplatilac,rezervisanaSredstva
	FROM banka_schema.racun
	WHERE "broj_racuna" = brojRacunaUplatioca FOR UPDATE;
	
	SELECT aktivan,currency
	INTO aktivan_primalac,valuta_primalac
	FROM banka_schema.racun
	WHERE "broj_racuna" = brojRacunaPrimaoca FOR UPDATE;
	
	IF(aktivan_uplatilac IS FALSE OR aktivan_primalac IS FALSE) THEN
		RETURN FALSE;
	END IF;
	IF(rezervisanaSredstva < iznosUplate) THEN
		RETURN FALSE;
	END IF;
	IF(valuta_uplatilac = valuta_primalac) THEN
		UPDATE banka_schema.racun SET "stanje" = "stanje" - iznosUplate 
		WHERE "broj_racuna" = brojRacunaUplatioca;

		UPDATE banka_schema.racun SET "stanje" = "stanje" + iznosUplate,
		"raspolozivo_stanje" = "raspolozivo_stanje" + iznosUplate 
		WHERE "broj_racuna" = brojRacunaPrimaoca;
		RETURN TRUE;
	END IF;
	
	SELECT currency INTO postojiValutaUplatilac FROM banka_schema.exchange_account 
	WHERE "currency" = valuta_uplatilac FOR UPDATE;
	IF(postojiValutaUplatilac IS NULL) THEN
		RETURN FALSE;
	END IF;
	
	SELECT currency,stanje INTO postojiValutaPrimalac,stanjeMenjacnicaPrimalac FROM banka_schema.exchange_account
	WHERE "currency" = valuta_primalac FOR UPDATE;
	IF(postojiValutaPrimalac IS NULL OR stanjeMenjacnicaPrimalac < iznosPrimaocu) THEN
		RETURN FALSE;
	END IF;
	
	UPDATE banka_schema.racun SET "stanje" = "stanje" - iznosUplate 
	WHERE "broj_racuna" = brojRacunaUplatioca;

	UPDATE banka_schema.racun SET "stanje" = "stanje" + iznosPrimaocu,
	"raspolozivo_stanje" = "raspolozivo_stanje" + iznosPrimaocu 
	WHERE "broj_racuna" = brojRacunaPrimaoca;
	
	UPDATE banka_schema.exchange_account SET "stanje" = "stanje" + iznosUplate 
	WHERE "currency" = valuta_uplatilac;

	UPDATE banka_schema.exchange_account SET "stanje" = "stanje" - iznosPrimaocu 
	WHERE "currency" = valuta_primalac;
	
	RETURN TRUE;
END $$ LANGUAGE plpgsql;
	
