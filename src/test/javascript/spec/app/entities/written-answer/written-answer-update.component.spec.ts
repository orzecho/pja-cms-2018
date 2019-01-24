/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { WrittenAnswerUpdateComponent } from 'app/entities/written-answer/written-answer-update.component';
import { WrittenAnswerService } from 'app/entities/written-answer/written-answer.service';
import { WrittenAnswer } from 'app/shared/model/written-answer.model';

describe('Component Tests', () => {
    describe('WrittenAnswer Management Update Component', () => {
        let comp: WrittenAnswerUpdateComponent;
        let fixture: ComponentFixture<WrittenAnswerUpdateComponent>;
        let service: WrittenAnswerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [WrittenAnswerUpdateComponent]
            })
                .overrideTemplate(WrittenAnswerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WrittenAnswerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WrittenAnswerService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WrittenAnswer(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.writtenAnswer = entity;
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
                    const entity = new WrittenAnswer();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.writtenAnswer = entity;
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
