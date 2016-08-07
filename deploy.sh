lein do clean, garden once, uberjar
scp target/mycotrack-frame.jar root@ocean:/usr/local/jar/mycotrack-frame.jar
ssh root@ocean 'service mycotrack-frame stop'
ssh root@ocean 'service mycotrack-frame start'
