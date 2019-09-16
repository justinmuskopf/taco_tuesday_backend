#!/bin/bash

PROJECT_DIR=/home/respec/workspace/taco_tuesday
SCRIPTS_DIR=$PROJECT_DIR/scripts

DB_NAME=taco_tuesday
DB_CONFIG_DIR=$SCRIPTS_DIR/config/db_config

EMPLOYEE_CONFIG_FILE=$DB_CONFIG_DIR/reset_employees
INDIVIDUAL_ORDER_CONFIG_FILE=$DB_CONFIG_DIR/reset_individual_orders
FULL_ORDER_CONFIG_FILE=$DB_CONFIG_DIR/reset_full_orders

MYSQL_CMD=/usr/bin/mysql
MYSQL_RESET_CMD="$MYSQL_CMD $DB_NAME"


function reset_table {
    echo Resetting table "$1"...
    $MYSQL_RESET_CMD < $1
}

function backup_db {
    /bin/bash $SCRIPTS_DIR/backup_db.sh
}

function reset_db {
    reset_table $EMPLOYEE_CONFIG_FILE
    reset_table $INDIVIDUAL_ORDER_CONFIG_FILE
    reset_table $FULL_ORDER_CONFIG_FILE
}

if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root!" 
   exit 1
fi


backup_db
reset_db
