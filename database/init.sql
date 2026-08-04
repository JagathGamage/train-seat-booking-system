IF DB_ID('train_booking') IS NULL
BEGIN
    CREATE DATABASE train_booking;
END
GO

USE train_booking;
GO