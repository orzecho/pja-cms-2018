import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IExam } from 'app/shared/model/exam.model';
import { ExamService } from './exam.service';
import { IUser, UserService } from 'app/core';
import { Word } from 'app/shared/model/word.model';
import { WordService } from 'app/entities/word';
import { TagService } from 'app/entities/tag';

@Component({
    selector: 'jhi-exam-update',
    templateUrl: './exam-update.component.html'
})
export class ExamUpdateComponent implements OnInit {
    private _exam: IExam;
    isSaving: boolean;

    users: IUser[];

    tags: string[];
    availableTags: string[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private examService: ExamService,
        private userService: UserService,
        private wordService: WordService,
        private activatedRoute: ActivatedRoute,
        private tagService: TagService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ exam }) => {
            this.exam = exam;
        });
        if (this.exam.words) {
            this.tags = this.getTagsFromWords(this.exam.words);
        }
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
        if (this.exam.id !== undefined) {
            this.subscribeToSaveResponse(this.examService.update(this.exam, this.tags));
        } else {
            this.subscribeToSaveResponse(this.examService.create(this.exam, this.tags));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IExam>>) {
        result.subscribe((res: HttpResponse<IExam>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    get exam() {
        return this._exam;
    }

    set exam(exam: IExam) {
        this._exam = exam;
    }
}
