import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { ITag } from 'app/shared/model/tag.model';
import { Principal } from 'app/core';
import { TestService } from 'app/entities/test/test.service';
import { FillingGapsTestItem, IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';
import { GapItem } from 'app/shared/model/gap-item.model';

@Component({
    selector: 'jhi-gaps-test',
    templateUrl: './gaps-test.component.html',
    styleUrls: ['./gaps-test.component.css']
})
export class GapsTestComponent implements OnInit {
    currentAccount: any;
    tags: string;
    fillingGapsTestItems: FillingGapsTestItem[];
    error: any;
    success: any;

    constructor(
        private testService: TestService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute
    ) {
        this.activatedRoute.params.subscribe(params => {
            this.tags = params.tags;
            this.loadAll();
        });
    }

    loadAll() {
        this.testService
            .getFillingGaps(this.tags.split(','))
            .subscribe(
                (res: HttpResponse<IFillingGapsTestItem[]>) => (this.fillingGapsTestItems = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    trackId(index: number, item: ITag) {
        return item.id;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    checkAnswer(gap: GapItem) {
        gap.answerCorrect = gap.value.split(',').some(v => v === gap.answerFromUser);
    }

    previousState() {
        window.history.back();
    }
}
