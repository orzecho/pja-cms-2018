import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IExamResult } from 'app/shared/model/exam-result.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ExamResultService } from './exam-result.service';
import { ExamService } from 'app/entities/exam';
import { IExam } from 'app/shared/model/exam.model';

@Component({
    selector: 'jhi-exam-result',
    templateUrl: './exam-result-for-teacher.component.html'
})
export class ExamResultForTeacherComponent implements OnInit, OnDestroy {
    currentAccount: any;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    teacherExams: IExam[];
    examResults: IExamResult[];

    constructor(
        private examResultService: ExamResultService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private examService: ExamService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        const examIds = this.teacherExams.map(exam => exam.id);
        this.examResultService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                'examId.in': examIds.join(',')
            })
            .subscribe(
                (res: HttpResponse<IExamResult[]>) => this.paginateExamResults(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        const examIds = this.teacherExams.map(exam => exam.id);
        this.router.navigate(['/exam-result-for-teacher'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
                'examId.in': examIds.join(',')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/exam-result-for-teacher',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.loadTeacherExams();
        });
        this.registerChangeInExamResults();
    }

    loadTeacherExams() {
        this.examService
            .query({
                'creatorId.equals': this.currentAccount.id
            })
            .subscribe(res => {
                this.teacherExams = res.body;
                this.loadAll();
            });
    }

    ngOnDestroy() {
        if (this.eventManager) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }

    trackId(index: number, item: IExamResult) {
        return item.id;
    }

    registerChangeInExamResults() {
        this.eventSubscriber = this.eventManager.subscribe('examResultListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateExamResults(data: IExamResult[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.examResults = data;
    }

    getMaxPoints(examResult: IExamResult) {
        let maxPoints = 0;
        if (examResult.writtenAnswers) {
            maxPoints = maxPoints + examResult.writtenAnswers.length;
        }
        if (examResult.trueFalseAnswers) {
            maxPoints = maxPoints + examResult.trueFalseAnswers.length;
        }
        return maxPoints;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
