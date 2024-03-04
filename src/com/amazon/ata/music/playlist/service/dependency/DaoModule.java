package com.amazon.ata.music.playlist.service.dependency;

import com.amazon.ata.aws.dynamodb.DynamoDbClientProvider;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DaoModule {
    @Singleton
    @Provides
    DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
    }
}
