import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OwnerDetailComponent } from './owner-detail.component';

describe('Owner Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OwnerDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OwnerDetailComponent,
              resolve: { owner: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OwnerDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load owner on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OwnerDetailComponent);

      // THEN
      expect(instance.owner).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
