package com.android.nitecafe.whirlpoolnews.dagger;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

@Module
public class SubjectModule {

    @Provides
    @Singleton @Named("whim") PublishSubject<Void> whimCounterSubject() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    @Named("search")
    BehaviorSubject<List<ScrapedThread>> searchResultsSubject() {
        return BehaviorSubject.create();
    }
}
