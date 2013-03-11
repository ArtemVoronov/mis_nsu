# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table doctor (
  id                        bigint not null,
  name                      varchar(255),
  surname                   varchar(255),
  patronymic                varchar(255),
  gender_id                 bigint,
  birthday                  timestamp,
  doctor_type_id            bigint,
  office_number             integer,
  constraint pk_doctor primary key (id))
;

create table doctor_type (
  id                        bigint not null,
  doctor_type_name          varchar(255),
  constraint pk_doctor_type primary key (id))
;

create table event (
  id                        bigint not null,
  title                     varchar(255),
  all_day                   boolean,
  start                     timestamp,
  end                       timestamp,
  ends_same_day             boolean,
  doctor_id                 bigint,
  patient_id                bigint,
  constraint pk_event primary key (id))
;

create table gender (
  id                        bigint not null,
  gender_code               smallint,
  constraint pk_gender primary key (id))
;

create table identity_doc (
  id                        bigint not null,
  identity_doc_type_id      bigint,
  number                    integer,
  series                    integer,
  date_issue                timestamp,
  who_issue                 varchar(255),
  constraint pk_identity_doc primary key (id))
;

create table identity_doc_type (
  id                        bigint not null,
  identity_doc_type_name    varchar(255),
  constraint pk_identity_doc_type primary key (id))
;

create table organization (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_organization primary key (id))
;

create table patient (
  id                        bigint not null,
  name                      varchar(255),
  surname                   varchar(255),
  patronymic                varchar(255),
  gender_id                 bigint,
  birthday                  timestamp,
  med_org                   varchar(255),
  insurance_num             varchar(255),
  benefit_code              varchar(255),
  snils                     varchar(255),
  residence_address         varchar(255),
  current_address           varchar(255),
  home_phone                varchar(255),
  work_phone                varchar(255),
  disable                   varchar(255),
  work_place                varchar(255),
  dependent                 varchar(255),
  blood                     varchar(255),
  is_address_the_same       boolean,
  identity_doc_id           bigint,
  constraint pk_patient primary key (id))
;

create table person (
  id                        bigint not null,
  name                      varchar(255),
  surname                   varchar(255),
  patronymic                varchar(255),
  gender_id                 bigint,
  birthday                  timestamp,
  constraint pk_person primary key (id))
;

create table account (
  id                        bigint not null,
  email                     varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_account primary key (id))
;

create sequence doctor_seq;

create sequence doctor_type_seq;

create sequence event_seq;

create sequence gender_seq;

create sequence identity_doc_seq;

create sequence identity_doc_type_seq;

create sequence organization_seq;

create sequence patient_seq;

create sequence person_seq;

create sequence account_seq;

alter table doctor add constraint fk_doctor_gender_1 foreign key (gender_id) references gender (id) on delete restrict on update restrict;
create index ix_doctor_gender_1 on doctor (gender_id);
alter table doctor add constraint fk_doctor_doctorType_2 foreign key (doctor_type_id) references doctor_type (id) on delete restrict on update restrict;
create index ix_doctor_doctorType_2 on doctor (doctor_type_id);
alter table event add constraint fk_event_doctor_3 foreign key (doctor_id) references doctor (id) on delete restrict on update restrict;
create index ix_event_doctor_3 on event (doctor_id);
alter table event add constraint fk_event_patient_4 foreign key (patient_id) references patient (id) on delete restrict on update restrict;
create index ix_event_patient_4 on event (patient_id);
alter table identity_doc add constraint fk_identity_doc_identityDocTyp_5 foreign key (identity_doc_type_id) references identity_doc_type (id) on delete restrict on update restrict;
create index ix_identity_doc_identityDocTyp_5 on identity_doc (identity_doc_type_id);
alter table patient add constraint fk_patient_gender_6 foreign key (gender_id) references gender (id) on delete restrict on update restrict;
create index ix_patient_gender_6 on patient (gender_id);
alter table patient add constraint fk_patient_identityDoc_7 foreign key (identity_doc_id) references identity_doc (id) on delete restrict on update restrict;
create index ix_patient_identityDoc_7 on patient (identity_doc_id);
alter table person add constraint fk_person_gender_8 foreign key (gender_id) references gender (id) on delete restrict on update restrict;
create index ix_person_gender_8 on person (gender_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists doctor;

drop table if exists doctor_type;

drop table if exists event;

drop table if exists gender;

drop table if exists identity_doc;

drop table if exists identity_doc_type;

drop table if exists organization;

drop table if exists patient;

drop table if exists person;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists doctor_seq;

drop sequence if exists doctor_type_seq;

drop sequence if exists event_seq;

drop sequence if exists gender_seq;

drop sequence if exists identity_doc_seq;

drop sequence if exists identity_doc_type_seq;

drop sequence if exists organization_seq;

drop sequence if exists patient_seq;

drop sequence if exists person_seq;

drop sequence if exists account_seq;

