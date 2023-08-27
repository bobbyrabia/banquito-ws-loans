/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     14/07/2023 17:06:34                          */
/*==============================================================*/


DROP INDEX IDX_AMOR_QUO_DAT;

DROP INDEX IDX_AMOR_REMA_BALA;

DROP TABLE AMORTIZATION;

DROP TABLE GUARANTY;

DROP INDEX IDX_LOAN_GRO_MEM;

DROP INDEX IDX_LOAN_CUS;

DROP INDEX IDX_LOAN_LOA_PRO_TYP;

DROP INDEX IDX_LOAN_ADV;

DROP TABLE LOAN;

DROP TABLE LOAN_TRANSACTION;

DROP TABLE LOAN_TRANSACTION_TYPE;

DROP INDEX IDX_REP_DUE;

DROP INDEX IDX_REP_PEN;

DROP INDEX IDX_REP_LOA_STA;

DROP TABLE REPAYMENT;

DROP TABLE TRANSACTION_CHANNEL;

DROP TABLE TRANSACTION_DETAIL;

/*==============================================================*/
/* Table: AMORTIZATION                                          */
/*==============================================================*/
CREATE TABLE AMORTIZATION (
   AMORTIZATION_ID      SERIAL               NOT NULL,
   LOAN_ID              INT4                 NULL,
   QUOTA_NUM            INT4                 NOT NULL,
   REMAINING_BALANCE    NUMERIC(18,2)        NOT NULL,
   QUOTA_CAPITAL        NUMERIC(18,2)        NOT NULL,
   QUOTA_INTEREST       NUMERIC(18,2)        NOT NULL,
   QUOTA_AMOUNT         NUMERIC(18,2)        NOT NULL,
   QUOTA_STATUS         VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_QUOTA_STATUS_AMORTIZA CHECK (QUOTA_STATUS IN ('CUR','PAI','NPA') AND QUOTA_STATUS = UPPER(QUOTA_STATUS)),
   CREATION_DATE        TIMESTAMP            NOT NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_AMORTIZATION PRIMARY KEY (AMORTIZATION_ID)
);

/*==============================================================*/
/* Index: IDX_AMOR_REMA_BALA                                    */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_AMOR_REMA_BALA ON AMORTIZATION (
AMORTIZATION_ID,
REMAINING_BALANCE
);

/*==============================================================*/
/* Index: IDX_AMOR_QUO_DAT                                      */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_AMOR_QUO_DAT ON AMORTIZATION (
AMORTIZATION_ID,
QUOTA_AMOUNT
);

/*==============================================================*/
/* Table: GUARANTY                                              */
/*==============================================================*/
CREATE TABLE GUARANTY (
   GUARANTY_ID          SERIAL               NOT NULL,
   CLIENT_ID            INT4                 NULL,
   GROUP_COMPANY_ID     INT4                 NULL,
   GROUP_ROLE_ID        VARCHAR(10)          NULL,
   GRO_CLIENT_ID        INT4                 NULL,
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   ASSET_NAME           VARCHAR(100)         NULL,
   TYPE                 VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TYPE_GUARANTY CHECK (TYPE IN ('GUA','ASS')),
   GUARANTOR_TYPE       VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_GUARANTOR_TYPE_GUARANTY CHECK (GUARANTOR_TYPE IN ('CLI','GRO','EXT')),
   STATE                VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATE_GUARANTY CHECK (STATE IN ('PEN','ACT','PBO','PSO','CLO','REV','TOT')),
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GUARANTY PRIMARY KEY (GUARANTY_ID)
);

/*==============================================================*/
/* Table: LOAN                                                  */
/*==============================================================*/
CREATE TABLE LOAN (
   LOAN_ID              SERIAL               NOT NULL,
   CLIENT_ID            INT4                 NULL,
   GROUP_COMPANY_ID     INT4                 NULL,
   GROUP_ROLE_ID        VARCHAR(10)          NULL,
   GRO_CLIENT_ID        INT4                 NULL,
   BRANCH_ID            INT4                 NULL,
   USER_ID              INT4                 NULL,
   LOAN_PRODUCT_ID      INT4                 NOT NULL,
   GUARANTY_ID          INT4                 NULL,
   RESCHEDULED_LOAN_ID  INT4                 NULL,
   ACCOUNT_HOLDER_TYPE  VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_ACCOUNT_HOLDER_TY_LOAN CHECK (ACCOUNT_HOLDER_TYPE IN ('CLI','GRO','COM')),
   CREATION_DATE        TIMESTAMP            NOT NULL,
   APPROVED_DATE        TIMESTAMP            NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   STATE                VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATE_LOAN CHECK (STATE IN ('ACT','ARR','APP','PEN','CWO','REJ','CLO')),
   NAME                 VARCHAR(100)         NOT NULL,
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   PRINCIPAL_DUE        NUMERIC(18,2)        NOT NULL,
   INTEREST_DUE         NUMERIC(18,2)        NOT NULL,
   INTEREST_PAID        NUMERIC(18,2)        NOT NULL,
   FEES_DUE             NUMERIC(18,2)        NOT NULL,
   FEES_PAID            NUMERIC(18,2)        NOT NULL,
   PENALTY_DUE          NUMERIC(18,2)        NOT NULL,
   PENALTY_PAID         NUMERIC(18,2)        NOT NULL,
   REPAYMENT_PERIOD_COUNT INT4                 NOT NULL,
   REPAYMENT_PERIOD_UNIT VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_REPAYMENT_PERIOD__LOAN CHECK (REPAYMENT_PERIOD_UNIT IN ('DAY','WEE','MON','YEA','BIA','QUA')),
   REPAYMENT_INSTALLMENTS INT4                 NOT NULL,
   GRACE_PERIOD_TYPE    VARCHAR(3)           NULL
      CONSTRAINT CKC_GRACE_PERIOD_TYPE_LOAN CHECK (GRACE_PERIOD_TYPE IS NULL OR (GRACE_PERIOD_TYPE IN ('NON','PAY','INT'))),
   GRACE_PERIOD         INT4                 NULL,
   INTEREST_RATE        NUMERIC(3,2)         NOT NULL,
   INTEREST_CHARGE_FREQUENCE VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_INTEREST_CHARGE_F_LOAN CHECK (INTEREST_CHARGE_FREQUENCE IN ('ANN','EVM','EFW','EVD')),
   INTEREST_CALCULATION_METHOD VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_INTEREST_CALCULAT_LOAN CHECK (INTEREST_CALCULATION_METHOD IN ('FLA','DBL','DBD')),
   NOTES                VARCHAR(1000)        NULL,
   LAST_ACCOUNT_APPRAISAL_DATE TIMESTAMP            NULL,
   SCHEDULE_DUE_DATES_METHOD VARCHAR(3)           NOT NULL,
   FIXED_DAYS_OF_MONTH  INT4                 NOT NULL,
   TAX_RATE             NUMERIC(3,2)         NOT NULL,
   PENALTY_RATE         NUMERIC(3,2)         NOT NULL,
   PRINCIPAL_PAID       NUMERIC(18,2)        NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN PRIMARY KEY (LOAN_ID)
);

/*==============================================================*/
/* Index: IDX_LOAN_ADV                                          */
/*==============================================================*/
CREATE  INDEX IDX_LOAN_ADV ON LOAN (
USER_ID
);

/*==============================================================*/
/* Index: IDX_LOAN_LOA_PRO_TYP                                  */
/*==============================================================*/
CREATE  INDEX IDX_LOAN_LOA_PRO_TYP ON LOAN (
LOAN_PRODUCT_ID
);

/*==============================================================*/
/* Index: IDX_LOAN_CUS                                          */
/*==============================================================*/
CREATE  INDEX IDX_LOAN_CUS ON LOAN (
LOAN_ID,
CLIENT_ID
);

/*==============================================================*/
/* Index: IDX_LOAN_GRO_MEM                                      */
/*==============================================================*/
CREATE  INDEX IDX_LOAN_GRO_MEM ON LOAN (
LOAN_ID,
GRO_CLIENT_ID
);

/*==============================================================*/
/* Table: LOAN_TRANSACTION                                      */
/*==============================================================*/
CREATE TABLE LOAN_TRANSACTION (
   LOAN_TRANSACTION_ID  SERIAL               NOT NULL,
   LOAN_ID              INT4                 NOT NULL,
   LOAN_TRANSACTION_TYPE_ID INT4                 NOT NULL,
   USER_ID              INT4                 NULL,
   BRANCH_ID            INT4                 NULL,
   COMMENT              VARCHAR(1000)        NULL
      CONSTRAINT CKC_COMMENT_LOAN_TRA CHECK (COMMENT IS NULL OR (COMMENT IN ('CRE','EDI','DIS','STC','REP'))),
   CREATION_DATE        TIMESTAMP            NOT NULL,
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   BALANCE_AFTER_TRS    NUMERIC(18,2)        NOT NULL,
   ENTRY_DATE           TIMESTAMP            NOT NULL,
   PRINCIPAL_AMOUNT     NUMERIC(18,2)        NOT NULL,
   INTEREST_AMOUNT      NUMERIC(18,2)        NOT NULL,
   FEES_AMOUNT          NUMERIC(18,2)        NOT NULL,
   PENALTY_AMOUNT       NUMERIC(18,2)        NOT NULL,
   PRINCIPAL_BALANCE    NUMERIC(18,2)        NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN_TRANSACTION PRIMARY KEY (LOAN_TRANSACTION_ID)
);

/*==============================================================*/
/* Table: LOAN_TRANSACTION_TYPE                                 */
/*==============================================================*/
CREATE TABLE LOAN_TRANSACTION_TYPE (
   LOAN_TRANSACTION_TYPE_ID SERIAL               NOT NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   DESCRIPTION          VARCHAR(1000)        NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN_TRANSACTION_TYPE PRIMARY KEY (LOAN_TRANSACTION_TYPE_ID)
);

/*==============================================================*/
/* Table: REPAYMENT                                             */
/*==============================================================*/
CREATE TABLE REPAYMENT (
   REPAYMENT_ID         SERIAL               NOT NULL,
   LOAN_ID              INT4                 NOT NULL,
   USER_ID              INT4                 NULL,
   BRANCH_ID            INT4                 NULL,
   ACCOUNT_TRANSACTION_ID INT4                 NOT NULL,
   STATE                VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATE_REPAYMEN CHECK (STATE IN ('PEN','LAT','PAI','PPA','RES','GRA')),
   DUE_DATE             TIMESTAMP            NOT NULL,
   REPAID_DATE          TIMESTAMP            NULL,
   LAST_PAID_DATE       TIMESTAMP            NULL,
   LAST_PENALTY_APPLIED_DATE TIMESTAMP            NULL,
   PRINCIPAL_DUE        NUMERIC(18,2)        NOT NULL,
   PRINCIPAL_PAID       NUMERIC(18,2)        NOT NULL,
   INTEREST_DUE         NUMERIC(18,2)        NOT NULL,
   INTEREST_PAID        NUMERIC(18,2)        NOT NULL,
   FEES_DUE             NUMERIC(18,2)        NULL,
   FEES_PAID            NUMERIC(18,2)        NULL,
   PENALTY_DUE          NUMERIC(18,2)        NULL,
   PENALTY_PAID         NUMERIC(18,2)        NULL,
   NOTES                VARCHAR(1000)        NULL,
   TAX_INTEREST_DUE     NUMERIC(18,2)        NULL,
   TAX_INTEREST_PAID    NUMERIC(18,2)        NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_REPAYMENT PRIMARY KEY (REPAYMENT_ID)
);

/*==============================================================*/
/* Index: IDX_REP_LOA_STA                                       */
/*==============================================================*/
CREATE  INDEX IDX_REP_LOA_STA ON REPAYMENT (
LOAN_ID,
STATE
);

/*==============================================================*/
/* Index: IDX_REP_PEN                                           */
/*==============================================================*/
CREATE  INDEX IDX_REP_PEN ON REPAYMENT (
PENALTY_DUE,
PENALTY_PAID
);

/*==============================================================*/
/* Index: IDX_REP_DUE                                           */
/*==============================================================*/
CREATE  INDEX IDX_REP_DUE ON REPAYMENT (
REPAYMENT_ID,
LOAN_ID,
DUE_DATE
);

/*==============================================================*/
/* Table: TRANSACTION_CHANNEL                                   */
/*==============================================================*/
CREATE TABLE TRANSACTION_CHANNEL (
   TRANSACTION_CHANNEL_ID SERIAL               NOT NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   ACTIVATED            BOOL                 NOT NULL,
   LOAN_CONSTRAINS_USAGE VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_LOAN_CONSTRAINS_U_TRANSACT CHECK (LOAN_CONSTRAINS_USAGE IN ('UNC','LIM')),
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_TRANSACTION_CHANNEL PRIMARY KEY (TRANSACTION_CHANNEL_ID)
);

/*==============================================================*/
/* Table: TRANSACTION_DETAIL                                    */
/*==============================================================*/
CREATE TABLE TRANSACTION_DETAIL (
   TRANSACTION_DETAIL_ID SERIAL               NOT NULL,
   LOAN_TRANSACTION_ID  INT4                 NOT NULL,
   TRANSACTION_CHANNEL_ID INT4                 NOT NULL,
   INTERNAL_TRANSFER    BOOL                 NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_TRANSACTION_DETAIL PRIMARY KEY (TRANSACTION_DETAIL_ID)
);

ALTER TABLE AMORTIZATION
   ADD CONSTRAINT FK_AMORTIZA_AMO_TO_LO_LOAN FOREIGN KEY (LOAN_ID)
      REFERENCES LOAN (LOAN_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_LOA_TO_GU_GUARANTY FOREIGN KEY (GUARANTY_ID)
      REFERENCES GUARANTY (GUARANTY_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_LOA_TO_LO_LOAN FOREIGN KEY (RESCHEDULED_LOAN_ID)
      REFERENCES LOAN (LOAN_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN_TRANSACTION
   ADD CONSTRAINT FK_LOAN_TRA_LOATRA_TO_LOAN_TRA FOREIGN KEY (LOAN_TRANSACTION_TYPE_ID)
      REFERENCES LOAN_TRANSACTION_TYPE (LOAN_TRANSACTION_TYPE_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN_TRANSACTION
   ADD CONSTRAINT FK_LOAN_TRA_LOATRS_TO_LOAN FOREIGN KEY (LOAN_ID)
      REFERENCES LOAN (LOAN_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE REPAYMENT
   ADD CONSTRAINT FK_REPAYMEN_REP_TO_LO_LOAN FOREIGN KEY (LOAN_ID)
      REFERENCES LOAN (LOAN_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE TRANSACTION_DETAIL
   ADD CONSTRAINT FK_TRANSACT_TRADET_TO_LOAN_TRA FOREIGN KEY (LOAN_TRANSACTION_ID)
      REFERENCES LOAN_TRANSACTION (LOAN_TRANSACTION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE TRANSACTION_DETAIL
   ADD CONSTRAINT FK_TRANSACT_TRADET_TO_TRANSACT FOREIGN KEY (TRANSACTION_CHANNEL_ID)
      REFERENCES TRANSACTION_CHANNEL (TRANSACTION_CHANNEL_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

