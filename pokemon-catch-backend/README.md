## Docker
to build:
`mvnw spring-boot:build-image`  
`docker-compose build`  
`docker-compose up -d`

   
To see the logs: `docker logs pokemon-catch-backend -f`

## AWS
Install AWS tools [here](https://docs.aws.amazon.com/powershell/latest/userguide/pstools-getting-set-up-windows.html).

Set up creds:
```
Set-AWSCredential `
                 -AccessKey <access key> `
                 -SecretKey <secret key> `
                 -StoreAs Admin
Initialize-AWSDefaultConfiguration -ProfileName Admin -Region eu-west-2
```

## Java
Need to have `-Dhttps.protocols=TLSv1.1,TLSv1.2` to run