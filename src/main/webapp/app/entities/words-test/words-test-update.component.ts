import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWordsTest } from 'app/shared/model/words-test.model';
import { WordsTestService } from './words-test.service';
import { UserService } from 'app/core';
import { WordService } from 'app/entities/word';
import { TagService } from 'app/entities/tag';
import { Word } from 'app/shared/model/word.model';

@Component({
    selector: 'jhi-words-test-update',
    templateUrl: './words-test-update.component.html'
})
export class WordsTestUpdateComponent implements OnInit {
    private _wordsTest: IWordsTest;
    isSaving: boolean;

    tags: string[];
    availableTags: string[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private wordsTestService: WordsTestService,
        private userService: UserService,
        private wordService: WordService,
        private activatedRoute: ActivatedRoute,
        private tagService: TagService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wordsTest }) => {
            this.wordsTest = wordsTest;
            if (this.wordsTest.words) {
                this.tags = this.getTagsFromWords(this.wordsTest.words);
            }
        });
    }

    private getTagsFromWords(words: Word[]) {
        const tag_names = [];
        words.map(word => word.tags).forEach(tags => tags.forEach(tag => tag_names.push(tag.name)));
        return Array.from(new Set(tag_names));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.wordsTest.id !== undefined) {
            this.subscribeToSaveResponse(this.wordsTestService.update(this.wordsTest, this.tags));
        } else {
            this.subscribeToSaveResponse(this.wordsTestService.create(this.wordsTest, this.tags));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWordsTest>>) {
        result.subscribe((res: HttpResponse<IWordsTest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    search(event) {
        const req = {
            'name.contains': event.query
        };
        this.tagService.query(req).subscribe(response => {
            this.availableTags = response.body.map(tag => tag.name);
            console.log(this.availableTags);
        });
    }

    get wordsTest() {
        return this._wordsTest;
    }

    set wordsTest(wordsTest: IWordsTest) {
        this._wordsTest = wordsTest;
    }
}
