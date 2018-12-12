import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IWord, Word } from 'app/shared/model/word.model';
import { ITag } from 'app/shared/model/tag.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { WordService } from './word.service';
import { TagService } from 'app/entities/tag';

@Component({
    selector: 'jhi-word',
    templateUrl: './word.component.html'
})
export class WordComponent implements OnInit, OnDestroy {
    currentAccount: any;
    words: IWord[];
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

    /** filter by tags */
    foundTags: ITag[];
    tagsFilter: ITag[];

    constructor(
        private wordService: WordService,
        private tagService: TagService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
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
        this.wordService
            .query(this.getRequestParams())
            .subscribe(
                (res: HttpResponse<IWord[]>) => this.paginateWords(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    getRequestParams() {
        const requestParams: any = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };
        if (this.tagsFilter && this.tagsFilter.length > 0) {
            requestParams['tagId.in'] = this.tagsFilter.map(tag => tag.id);
        }
        return requestParams;
    }

    transition() {
        this.router.navigate(['/word'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/word',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWords();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWord) {
        return item.id;
    }

    registerChangeInWords() {
        this.eventSubscriber = this.eventManager.subscribe('wordListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    searchForTags(event) {
        this.tagService
            .query({ 'name.contains': event.query })
            .subscribe((res: HttpResponse<ITag[]>) => (this.foundTags = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    }

    private paginateWords(data: IWord[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.words = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    getJishoLink(word: Word) {
        return 'https://jisho.org/search/'.concat(word.kanji ? word.kanji : word.kana);
    }
}
