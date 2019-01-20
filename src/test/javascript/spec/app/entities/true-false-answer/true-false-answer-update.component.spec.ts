/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { TrueFalseAnswerUpdateComponent } from 'app/entities/true-false-answer/true-false-answer-update.component';
import { TrueFalseAnswerService } from 'app/entities/true-false-answer/true-false-answer.service';
import { TrueFalseAnswer } from 'app/shared/model/true-false-answer.model';

describe('Component Tests', () => {
    describe('TrueFalseAnswer Management Update Component', () => {
        let comp: TrueFalseAnswerUpdateComponent;
        let fixture: ComponentFixture<TrueFalseAnswerUpdateComponent>;
        let service: TrueFalseAnswerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [TrueFalseAnswerUpdateComponent]
            })
                .overrideTemplate(TrueFalseAnswerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TrueFalseAnswerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrueFalseAnswerService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new TrueFalseAnswer(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.trueFalseAnswer = entity;
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
                    const entity = new TrueFalseAnswer();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.trueFalseAnswer = entity;
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
