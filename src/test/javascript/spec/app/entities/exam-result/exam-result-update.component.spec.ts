/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { ExamResultUpdateComponent } from 'app/entities/exam-result/exam-result-update.component';
import { ExamResultService } from 'app/entities/exam-result/exam-result.service';
import { ExamResult } from 'app/shared/model/exam-result.model';

describe('Component Tests', () => {
    describe('ExamResult Management Update Component', () => {
        let comp: ExamResultUpdateComponent;
        let fixture: ComponentFixture<ExamResultUpdateComponent>;
        let service: ExamResultService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [ExamResultUpdateComponent]
            })
                .overrideTemplate(ExamResultUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ExamResultUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExamResultService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ExamResult(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.examResult = entity;
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
                    const entity = new ExamResult();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.examResult = entity;
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
