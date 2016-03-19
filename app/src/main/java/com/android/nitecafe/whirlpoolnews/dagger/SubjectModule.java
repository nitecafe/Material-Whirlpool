package com.android.nitecafe.whirlpoolnews.dagger;

import android.net.Uri;
import android.os.Bundle;

import java.util.List;

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

    @Provides
    @Singleton
    @Named("browser")
    PublishSubject<Uri> launchBrowserSubject() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    @Named("prefetch")
    PublishSubject<Uri> prefetchLinkSubject() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    @Named("prefetchBundle")
    PublishSubject<List<Bundle>> prefetchBundleSubject() {
        return PublishSubject.create();
    }
}
