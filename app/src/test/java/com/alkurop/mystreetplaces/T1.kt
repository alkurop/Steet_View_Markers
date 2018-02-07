package com.alkurop.mystreetplaces

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test
import java.util.concurrent.TimeUnit

class T1 {
    @Test
    fun myTEst() {
        val fromCallable = Completable.fromCallable {
            Thread.sleep(2000)
            println("completed")
        }.toObservable<Long>()

        val interval = Observable.interval(100, TimeUnit.MILLISECONDS)

        val resultBus = PublishSubject.create<Long>()
        val observer = Subscriber<Long>(resultBus)

        fromCallable.subscribeOn(Schedulers.io())
                .subscribeWith(observer)
        interval.subscribeOn(Schedulers.io())
                .subscribeWith(observer)

        resultBus.subscribe { println((it + 1) * 100) }

        Thread.sleep(4000)


    }
}

internal class Subscriber<T>(
        val resultBus: Subject<T>
) : Observer<T> {
    override fun onSubscribe(d: Disposable) {

    }

    override fun onComplete() {
        resultBus.onComplete()
        println("Sub completed")
    }

    override fun onNext(value: T) {
        resultBus.onNext(value)
    }

    override fun onError(e: Throwable) {
        resultBus.onError(e)
    }
}
