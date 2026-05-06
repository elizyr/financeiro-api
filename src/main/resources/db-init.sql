-- ============================================================
-- Script de criação do banco de dados
-- Execute no PostgreSQL antes de subir a aplicação
-- ============================================================

CREATE DATABASE financeiro_db;

-- Obs: as tabelas são criadas automaticamente pelo Hibernate
-- (spring.jpa.hibernate.ddl-auto=update)
-- Este script cria apenas o banco.

-- Para conectar:
-- psql -U postgres
-- \c financeiro_db
