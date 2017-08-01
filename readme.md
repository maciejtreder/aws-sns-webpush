# AWS Webpush

Service built for filling gap in AWS Simple Notification Service - lack of support for push notifications in web browsers (Chrome, Firefox, Opera & **Safari**).
Service can be deployed on AWS Lambda. It is listening for SNS events, on fires on them.


## Deploy

There are several steps to start using this service:
* Install maven - for building Java project
* Install node - for easy deployment with Serverless framework
* VAPID_PUBLIC_KEY & VAPID_PRIVATE_KEY environment variables - can be generated with [Push Companion app](https://web-push-codelab.appspot.com)
* SAFARI_KEY_PASSWORD environment variable - used to protect your Safari key

```sh
export VAPID_PUBLIC_KEY=your_public_key
export VAPID_PRIVATE_KEY=your_private_key
export SAFARI_KEY_PASSWORD=safari_key_password

git clone https://github.com/maciejtreder/aws-sns-webpush.git
cd aws-sns-webpush
npm install
npm deploy
```


## Customization

There is a plan, to support customization of this service (enabling only vapid subscription, or only safari), you can track it in [this issue](https://github.com/maciejtreder/aws-sns-webpush/issues/2).

## Vapid push notifications

TODO


## Safari push notifications

todo