/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { LessonFileUpdateComponent } from 'app/entities/lesson-file/lesson-file-update.component';
import { LessonFileService } from 'app/entities/lesson-file/lesson-file.service';
import { LessonFile } from 'app/shared/model/lesson-file.model';

describe('Component Tests', () => {
    describe('LessonFile Management Update Component', () => {
        let comp: LessonFileUpdateComponent;
        let fixture: ComponentFixture<LessonFileUpdateComponent>;
        let service: LessonFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [LessonFileUpdateComponent]
            })
                .overrideTemplate(LessonFileUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(LessonFileUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LessonFileService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new LessonFile(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.lessonFile = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new LessonFile();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.lessonFile = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
