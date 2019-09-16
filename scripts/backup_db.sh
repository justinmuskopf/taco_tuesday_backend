#!/bin/bash

PROJECT_DIR=/home/respec/workspace/taco_tuesday
BACKUP_DIR=$PROJECT_DIR/backup/db_backup
CHOWN_CMD="chown respec:respec"

DB_NAME=taco_tuesday

MYSQL_CMD=/usr/bin/mysqldump
MYSQL_BACKUP_CMD="$MYSQL_CMD $DB_NAME"

function generate_backup_filename {
    echo "$BACKUP_DIR/`date +%m-%d-%Y_%H%M%S`.sql"
}

function backup_db {
    local backupFile=`generate_backup_filename`
    echo Backing up DB "$DB_NAME" to file "$backupFile"
    
    $MYSQL_BACKUP_CMD > $backupFile
    $CHOWN_CMD $backupFile
}

if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root!"
   exit 1
fi

backup_db
