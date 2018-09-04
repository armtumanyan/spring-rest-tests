INSERT INTO transaction( id, account_id , number , balance ) VALUES ( '1' , '1' , '12151885120' , 42.12 );
INSERT INTO transaction( id, account_id , number , balance ) VALUES ( '2' , '1' , '12151885121' ,456.00 );
INSERT INTO transaction( id, account_id , number , balance ) VALUES ( '3' , '1' , '12151885122' ,-12.12 );

INSERT INTO account( id, number, type, balance, creation_date, is_active ) VALUES ( '1', '01000251215', 'SAVING', 4210.42, '2018-09-05' ,true );
INSERT INTO account( id, number, type, balance, creation_date, is_active ) VALUES( '2', '01000251216', 'CURRENT', 25.12, '2018-08-05', false );