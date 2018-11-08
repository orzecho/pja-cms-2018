import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ILessonFile } from 'app/shared/model/lesson-file.model';
import { LessonFileService } from './lesson-file.service';
import { ILesson } from 'app/shared/model/lesson.model';
import { LessonService } from 'app/entities/lesson';

@Component({
    selector: 'jhi-lesson-file-update',
    templateUrl: './lesson-file-update.component.html'
})
export class LessonFileUpdateComponent implements OnInit {
    private _lessonFile: ILessonFile;
    isSaving: boolean;

    lessons: ILesson[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private lessonFileService: LessonFileService,
        private lessonService: LessonService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ lessonFile }) => {
            this.lessonFile = lessonFile;
        });
        this.lessonService.query().subscribe(
            (res: HttpResponse<ILesson[]>) => {
                this.lessons = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.lessonFile.id !== undefined) {
            this.subscribeToSaveResponse(this.lessonFileService.update(this.lessonFile));
        } else {
            this.subscribeToSaveResponse(this.lessonFileService.create(this.lessonFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILessonFile>>) {
        result.subscribe((res: HttpResponse<ILessonFile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackLessonById(index: number, item: ILesson) {
        return item.id;
    }
    get lessonFile() {
        return this._lessonFile;
    }

    set lessonFile(lessonFile: ILessonFile) {
        this._lessonFile = lessonFile;
    }
}
