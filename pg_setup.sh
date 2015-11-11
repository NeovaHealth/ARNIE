#!/bin/bash
export DBUSER="nhadt"
createuser --username postgres --createdb --no-createrole --no-superuser $DBUSER
psql --username postgres -c "alter role $DBUSER with password '$DBUSER'"
createdb $DBUSER --owner=$DBUSER -U postgres
psql -c 'CREATE TABLE MSG_HISTORY (id TEXT, TYPE VARCHAR(10), HOSPITAL_NUMBER TEXT, VISIT_ID TEXT,timestamp VARCHAR(100), data TEXT);' -U $DBUSER
psql -c 'CREATE TABLE FAIL_MSG_HISTORY (id TEXT, TYPE VARCHAR(10), HOSPITAL_NUMBER TEXT, VISIT_ID TEXT, timestamp VARCHAR(100), exception TEXT, data TEXT);' -U $DBUSER
psql -c 'ALTER TABLE MSG_HISTORY OWNER TO '$DBUSER';' -U $DBUSER
psql -c 'ALTER TABLE FAIL_MSG_HISTORY OWNER TO '$DBUSER';' -U $DBUSER
