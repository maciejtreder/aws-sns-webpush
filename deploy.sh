push_package -w pushPackage.raw/website.json -i pushPackage.raw/icon.iconset -c src/main/resources/cert.p12 -p $SAFARI_KEY_PASSWORD
mv pushPackage.zip src/main/resources/web.com.maciejtreder.angular-universal-serverless.zip
mvn clean package
serverless deploy
