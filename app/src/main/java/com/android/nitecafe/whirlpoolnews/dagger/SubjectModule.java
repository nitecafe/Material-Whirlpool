package com.android.nitecafe.whirlpoolnews.dagger;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subjects.PublishSubject;

@Module
public class SubjectModule {

    @Provides
    @Singleton @Named("whim") PublishSubject<Void> whimCounterSubject() {
        return PublishSubject.create();
    }
}
