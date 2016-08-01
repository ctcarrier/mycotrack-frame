lein do clean, ring uberjar
scp target/mycotrack-reagent.jar root@162.243.65.130:/usr/local/jar
ssh root@162.243.65.130 'service mycotrack-reagent stop'
ssh root@162.243.65.130 'service mycotrack-reagent start'
