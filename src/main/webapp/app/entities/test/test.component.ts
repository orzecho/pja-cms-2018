import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-tag',
    templateUrl: './test.component.html'
})
export class TestComponent implements OnInit, OnDestroy {
    currentAccount: any;
    tags: string;
    error: any;
    success: any;
    eventSubscriber: Subscription;

    constructor(
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInTags();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    routeToTestPage() {
        this.router.navigate([`/vocabulary/${this.tags}`]);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
